package forest.core.backend.httpclient.request;

import com.dtflys.forest.backend.httpclient.conn.HttpclientConnectionManager;
import com.dtflys.forest.backend.httpclient.response.HttpclientForestResponseFactory;
import com.dtflys.forest.backend.httpclient.response.HttpclientResponseHandler;
import com.dtflys.forest.exceptions.ForestNetworkException;
import com.dtflys.forest.exceptions.ForestRetryException;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.handler.LifeCycleHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.http.ForestResponseFactory;
import com.dtflys.forest.logging.ForestLogHandler;
import com.dtflys.forest.logging.LogConfiguration;
import com.dtflys.forest.logging.ResponseLogMessage;
import forest.core.backend.AbstractHttpExecutor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Date;

public class SyncHttpclientRequestSender extends AbstractHttpclientRequestSender {

  private HttpClient client;

  public SyncHttpclientRequestSender(HttpclientConnectionManager connectionManager, ForestRequest request) {
    super(connectionManager, request);
  }

  protected HttpClient getHttpClient(LifeCycleHandler lifeCycleHandler) {
    HttpClient client = connectionManager.getHttpClient(request, lifeCycleHandler);
    return client;
  }

  public void logResponse(ForestResponse response) {
    LogConfiguration logConfiguration = request.getLogConfiguration();
    if (!logConfiguration.isLogEnabled() || response.isLogged()) {
      return;
    }
    response.setLogged(true);
    ResponseLogMessage logMessage = new ResponseLogMessage(response, response.getStatusCode());
    ForestLogHandler logHandler = logConfiguration.getLogHandler();
    if (logHandler != null) {
      if (logConfiguration.isLogResponseStatus()) {
        logHandler.logResponseStatus(logMessage);
      }
      if (logConfiguration.isLogResponseContent()) {
        logHandler.logResponseContent(logMessage);
      }
    }
  }

  @Override
  public void sendRequest(ForestRequest request, AbstractHttpExecutor executor, HttpclientResponseHandler responseHandler, HttpUriRequest httpRequest, LifeCycleHandler lifeCycleHandler, Date startDate) throws IOException {
    HttpResponse httpResponse = null;
    ForestResponse response = null;
    HttpContext httpContext = new BasicHttpContext();
    httpContext.setAttribute("REQUEST", request);

    HttpClientContext httpClientContext = HttpClientContext.adapt(httpContext);

    client = getHttpClient(lifeCycleHandler);

    ForestResponseFactory forestResponseFactory = new HttpclientForestResponseFactory();
    try {
      logRequest(request.getCurrentRetryCount(), (HttpRequestBase) httpRequest, client);
      httpResponse = client.execute(httpRequest, httpClientContext);
    } catch (Throwable e) {
      response = forestResponseFactory.createResponse(request, httpResponse, lifeCycleHandler, e, startDate);
      if (httpRequest.isAborted()) {
        lifeCycleHandler.handleCanceled(request, response);
        return;
      }
      ForestRetryException retryException = new ForestRetryException(
        e,  request, request.getRetryCount(), request.getCurrentRetryCount());
      try {
        request.canRetry(response, retryException);
      } catch (Throwable throwable) {
        response = forestResponseFactory.createResponse(request, httpResponse, lifeCycleHandler, throwable, startDate);
        lifeCycleHandler.handleSyncWithException(request, response, e);
        return;
      }
      response = forestResponseFactory.createResponse(request, httpResponse, lifeCycleHandler, null, startDate);
      logResponse(response);
      executor.execute(lifeCycleHandler);
      return;
    } finally {
      if (response == null) {
        response = forestResponseFactory.createResponse(request, httpResponse, lifeCycleHandler, null, startDate);
      }
      logResponse(response);
    }

    if (response == null) {
      response = forestResponseFactory.createResponse(request, httpResponse, lifeCycleHandler, null, startDate);
    }
    // 检查是否重试
    ForestRetryException retryEx = request.canRetry(response);
    if (retryEx != null && retryEx.isNeedRetry() && !retryEx.isMaxRetryCountReached()) {
      response.close();
      executor.execute(lifeCycleHandler);
      return;
    }

    // 检查响应是否失败
    if (retryEx == null && response.isError()) {
      ForestNetworkException networkException =
        new ForestNetworkException("", response.getStatusCode(), response);
      ForestRetryException retryException = new ForestRetryException(
        networkException,  request, request.getRetryCount(), request.getCurrentRetryCount());
      try {
        request.canRetry(response, retryException);
      } catch (Throwable throwable) {
        responseHandler.handleSync(httpResponse, response);
        return;
      }
      response.close();
      executor.execute(lifeCycleHandler);
      return;
    }

    try {
      responseHandler.handleSync(httpResponse, response);
    } catch (Exception e) {
      if (e instanceof ForestRuntimeException) {
        throw e;
      } else {
        throw new ForestRuntimeException(e);
      }
    }
  }
}
