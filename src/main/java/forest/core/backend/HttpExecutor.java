package forest.core.backend;

import com.dtflys.forest.backend.ResponseHandler;
import com.dtflys.forest.handler.LifeCycleHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponseFactory;

public interface HttpExecutor {

  ForestRequest getRequest();

  void execute(LifeCycleHandler lifeCycleHandler);

  ResponseHandler getResponseHandler();

  ForestResponseFactory getResponseFactory();

  void close();
}
