package forest.core;

import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;

/**
 * https://github.com/dromara/forest/blob/master/forest-spring-boot3-starter/src/main/java/com/dtflys/forest/springboot/properties/ForestConvertProperties.java
 * 通用客户端接口
 */
public interface ForestGenericClient {

  @Request("/")
  ForestRequest<?> request();

  @Request("/")
  <T> ForestResponse<T> request(Class<T> clazz);
}
