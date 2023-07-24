package forest.core.http;

import com.dtflys.forest.http.ForestHeaderMap;
import com.dtflys.forest.http.ForestURL;
import com.dtflys.forest.http.HasHeaders;
import com.dtflys.forest.http.HasURL;

public class ForestRequest<T> implements HasURL, HasHeaders {
  private final static Object[] EMPTY_RENDER_ARGS = new Object[0];
  /**
   * 默认上传/下载进度监听的步长
   * 每上传/下载一定的比特数，执行一次监听回调函数
   */
  private final static long DEFAULT_PROGRESS_STEP = 1024 * 10;

  @Override
  public ForestHeaderMap getHeaders() {
    return null;
  }

  @Override
  public ForestURL url() {
    return null;
  }
}
