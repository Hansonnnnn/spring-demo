package com.example.springdemo.seata.config;

import java.util.concurrent.ExecutorService;

public interface ConfigChangeListener {
  ExecutorService getExecutor();

  void receiveConfigInfo(final String configInfo);
}
