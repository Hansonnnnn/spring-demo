package forest.core.http;

import com.dtflys.forest.auth.BasicAuth;
import com.dtflys.forest.auth.ForestAuthenticator;
import com.dtflys.forest.backend.HttpBackend;
import com.dtflys.forest.backend.HttpExecutor;
import com.dtflys.forest.callback.OnCanceled;
import com.dtflys.forest.callback.OnError;
import com.dtflys.forest.callback.OnLoadCookie;
import com.dtflys.forest.callback.OnProgress;
import com.dtflys.forest.callback.OnRedirection;
import com.dtflys.forest.callback.OnRetry;
import com.dtflys.forest.callback.OnSaveCookie;
import com.dtflys.forest.callback.OnSuccess;
import com.dtflys.forest.callback.RetryWhen;
import com.dtflys.forest.callback.SuccessWhen;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.converter.ForestConverter;
import com.dtflys.forest.handler.LifeCycleHandler;
import com.dtflys.forest.http.ForestAsyncMode;
import com.dtflys.forest.http.ForestBody;
import com.dtflys.forest.http.ForestHeaderMap;
import com.dtflys.forest.http.ForestProtocol;
import com.dtflys.forest.http.ForestProxy;
import com.dtflys.forest.http.ForestQueryMap;
import com.dtflys.forest.http.ForestRequestType;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.http.ForestURL;
import com.dtflys.forest.http.HasHeaders;
import com.dtflys.forest.http.HasURL;
import com.dtflys.forest.http.Lazy;
import com.dtflys.forest.interceptor.InterceptorAttributes;
import com.dtflys.forest.interceptor.InterceptorChain;
import com.dtflys.forest.logging.LogConfiguration;
import com.dtflys.forest.logging.RequestLogMessage;
import com.dtflys.forest.reflection.ForestMethod;
import com.dtflys.forest.retryer.ForestRetryer;
import com.dtflys.forest.ssl.SSLKeyStore;
import com.dtflys.forest.ssl.SSLSocketFactoryBuilder;
import com.dtflys.forest.utils.ForestDataType;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.TrustManager;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class ForestRequest<T> implements HasURL, HasHeaders {
  private final static Object[] EMPTY_RENDER_ARGS = new Object[0];
  /**
   * 默认上传/下载进度监听的步长
   * 每上传/下载一定的比特数，执行一次监听回调函数
   */
  private final static long DEFAULT_PROGRESS_STEP = 1024 * 10;

  private final ForestConfiguration configuration;
  private final ForestMethod method;
  private HttpBackend backend;
  /**
   * HTTP 后端 Client 对象或者对象工厂
   */
  private Object backendClient;
  /**
   * 是否缓存 HTTP 后端 Client 对象
   */
  private boolean cacheBackendClient = true;
  private HttpExecutor executor;
  private volatile LifeCycleHandler lifeCycleHandler;
  private ForestProtocol protocol = ForestProtocol.HTTP_1_1;
  /**
   * 请求是否取消
   */
  private volatile boolean canceled = false;
  private ForestURL url;
//  private ForestQueryMap query = new ForestQueryMap(this);

  private ForestRequestType type;
  /**
   * 请求类型变更历史
   */
  private List<ForestRequestType> typeChangeHistory;
  /**
   * 请求日志消息
   */
  private RequestLogMessage requestLogMessage;
  private String charset;
  private String responseEncode;
  private boolean async;
  /**
   * 异步请求模式，只有在 async = true 时有效
   */
  private ForestAsyncMode asyncMode = ForestAsyncMode.PLATFORM;
  private ForestAuthenticator authenticator = new BasicAuth();
  /**
   * 是否自动打开重定向
   */
  private boolean autoRedirection;
  /**
   * 响应数据类型，决定响应返回的数据以何种方式进行反序列化
   */
  private ForestDataType dataType;
  private int timeout = 3000;
  private Integer connectTimeout = -1;
  private Integer readTimeout = -1;
  /**
   * 是否开启解压 GZIP 响应内容
   */
  private boolean decompressResponseGzipEnable = false;
  /**
   * SSL 协议
   * 该字段在单项 HTTPS 请求发送时决定哪种 SSL 协议
   */
  private String sslProtocol;
  private int maxRetryCount = 0;
  /**
   * 最大请求重试的时间间隔，时间单位ms
   */
  private long maxRetryInterval;
//  private final ForestBody body;
  private ForestHeaderMap headers= new ForestHeaderMap(this);
  /**
   * 文件名
   */
  private String filename;
  /**
   * 参数列表，接口方法调用时传入的参数列表
   */
  private final Object[] arguments;
  /**
   * 请求成功时调用回调函数
   */
  private OnSuccess onSuccess;
  private OnError onError;
  private OnCanceled onCanceled;
  /**
   * 请求是否成功，如果成功执行 onSuccess，失败执行 onError
   */
  private SuccessWhen successWhen;
  private OnRetry onRetry;
  private RetryWhen retryWhen;
  private OnRedirection onRedirection;

  /**
   * 重定向的上一个请求
   */
  ForestRequest<?> prevRequest;
  /**
   * 重定向的上一个响应
   */
  ForestResponse<?> prevResponse;
  /**
   * 上传/下载进度监听时调用
   * 每上传/下载传输 ${progressStep} 个比特数时，执行一次回调函数
   */
  private OnProgress onProgress;
  private OnLoadCookie onLoadCookie;
  private OnSaveCookie onSaveCookie;

  private boolean isDownloadFile = false;

  private long progressStep = DEFAULT_PROGRESS_STEP;

  /**
   * 拦截器链
   */
  private InterceptorChain interceptorChain = new InterceptorChain();

  /**
   * 拦截器属性
   */
  private Map<Class, InterceptorAttributes> interceptorAttributes = new ConcurrentHashMap<>();

  /**
   * 请求重试策略
   * 通过该自定重试策略
   */
  private ForestRetryer retryer;

  private boolean retryEnabled = true;

  /**
   * 附件
   * 附件信息不会随请求发送到远端服务器，但在本地任何地方都可通过请求对象访问到
   */
  private Map<String, Object> attachments = new ConcurrentHashMap<>();

  /**
   * 当前正求值的延迟参数堆栈
   */
  Stack<Lazy> evaluatingLazyValueStack = new Stack<>();

  /**
   * 反序列化器
   */
  private ForestConverter decoder;

  /**
   * 请求日志配置信息
   */
  private LogConfiguration logConfiguration;

  /**
   * 双向 HTTPS 请求中使用的验证信息
   */
  private SSLKeyStore keyStore;

  /**
   * SSL 主机名 / 域名验证器
   */
  private HostnameVerifier hostnameVerifier;

  /**
   * SSL 信任管理器
   */
  private TrustManager trustManager;

  /**
   * SSL Socket 工厂构造器
   */
  private SSLSocketFactoryBuilder sslSocketFactoryBuilder;

  /**
   * 正向代理
   */
  private ForestProxy proxy;

  public ForestRequest(ForestConfiguration configuration, ForestMethod method, ForestBody body, Object[] arguments) {
    this.configuration = configuration;
    this.method = method;
    this.arguments = arguments;
//    this.body = body;
  }


  public ForestRequest(ForestConfiguration configuration, ForestMethod method, Object[] arguments) {
    this.configuration = configuration;
    this.method = method;
    this.arguments = arguments;
//    this.body = new ForestBody(this);
  }

  public ForestRequest(ForestConfiguration configuration, ForestMethod method) {
    this(configuration, method, new Object[0]);
  }

  public ForestRequest(ForestConfiguration configuration) {
    this(configuration, null, new Object[0]);
  }

  public ForestRequest(ForestConfiguration configuration, Object[] arguments) {
    this(configuration, null, arguments);
  }

  @Override
  public ForestHeaderMap getHeaders() {
    return null;
  }

  @Override
  public ForestURL url() {
    return null;
  }
}
