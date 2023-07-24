package forest.core;

import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.http.ForestFuture;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Forest 快捷接口
 */
public abstract class Forest {
  public final static String VERSION = "";

  public static ForestConfiguration config() {
    return ForestConfiguration.configuration();
  }

  public static ForestConfiguration config(String id) {
    return ForestConfiguration.configuration(id);
  }

  public static <T> T client(Class<T> clazz) {
    return config().createInstance(clazz);
  }

  public static ForestRequest<?> request() {
    return config().request();
  }

  public static <R> ForestRequest<R> request(Class<R> clazz) {
    return config().request(clazz);
  }

  public static ForestRequest<?> get(String url) {
    return config().get(url);
  }

  public static ForestRequest<?> post(String url) {
    return config().post(url);
  }

  public static ForestRequest<?> put(String url) {
    return config().put(url);
  }

  public static ForestRequest<?> delete(String url) {
    return config().delete(url);
  }

  public static ForestRequest<?> head(String url) {
    return config().head(url);
  }

  public static ForestRequest<?> patch(String url) {
    return config().patch(url);
  }
  // options trace

  public static List<ForestResponse> await(ForestFuture ...futures) {
    return Arrays.stream(futures).map(ForestFuture::await)
      .collect(Collectors.toList());
  }

  public static void await(Collection<ForestFuture> futures, Consumer<ForestResponse> callback) {
    for (ForestFuture future : futures) {
      future.await();
    }
    for (ForestFuture future : futures) {
      callback.accept(future.getResponse());
    }
  }
}
