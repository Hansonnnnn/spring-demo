package forest.core.backend.httpclient;

import com.dtflys.forest.annotation.Backend;
import com.dtflys.forest.annotation.MethodLifeCycle;
import com.dtflys.forest.backend.httpclient.HttpClientLifeCycle;
import com.dtflys.forest.backend.httpclient.HttpClientProvider;
import com.dtflys.forest.backend.httpclient.HttpclientBackend;
import com.dtflys.forest.backend.httpclient.conn.HttpclientConnectionManager;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Backend(HttpclientBackend.NAME)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@MethodLifeCycle(HttpClientLifeCycle.class)
public @interface HttpClient {

  Class<? extends HttpClientProvider> client() default HttpclientConnectionManager.DefaultHttpClientProvider.class;
}
