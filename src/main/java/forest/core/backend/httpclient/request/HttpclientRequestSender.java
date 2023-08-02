package forest.core.backend.httpclient.request;

import com.dtflys.forest.backend.httpclient.response.HttpclientResponseHandler;
import com.dtflys.forest.handler.LifeCycleHandler;
import com.dtflys.forest.http.ForestRequest;
import forest.core.backend.AbstractHttpExecutor;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.util.Date;

public interface HttpclientRequestSender {
  void sendRequest(
    ForestRequest request,
    AbstractHttpExecutor executor,
    HttpclientResponseHandler responseHandler,
    HttpUriRequest httpRequest,
    LifeCycleHandler lifeCycleHandler,
    Date startDate
  ) throws IOException;
}
