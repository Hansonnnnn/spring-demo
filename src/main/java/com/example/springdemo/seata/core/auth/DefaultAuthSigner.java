package com.example.springdemo.seata.core.auth;

import io.micrometer.common.util.StringUtils;

//@LoadLevel(name = "defaultAuthSigner", order = 100)
public class DefaultAuthSigner implements AuthSigner {

  @Override
  public String sign(String data, String key) {
    if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(data)) {
      return RamSignAdapter.getRamSign(data, key);
    }
    return data;
  }

  @Override
  public String getSignVersion() {
    return "V4";
  }
}
