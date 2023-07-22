package com.example.springdemo.seata.core.auth;

public interface AuthSigner {
  String sign(String data, String key);

  default String getSignVersion() {
    return null;
  }

}
