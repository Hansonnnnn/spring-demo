package forest.core.http;

import com.dtflys.forest.http.ForestQueryMap;
import com.dtflys.forest.http.ForestQueryParameter;
import lombok.Data;

@Data
public abstract class AbstractQueryParameter<SELF extends AbstractQueryParameter<SELF>> implements ForestQueryParameter<SELF> {

  protected final SELF self = (SELF) this;

  ForestQueryMap queries;
  /**
   * 是否做 url encode
   */
  protected boolean urlencoded = false;

  protected String charset;
  protected String defaultValue;
  /**
   * 是否源自 url
   */
  private final boolean fromUrl;

  protected AbstractQueryParameter(ForestQueryMap queries, boolean fromUrl) {
    this.queries = queries;
    this.fromUrl = fromUrl;
  }


}
