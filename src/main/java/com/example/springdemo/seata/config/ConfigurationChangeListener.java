package com.example.springdemo.seata.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public interface ConfigurationChangeListener {
  int CORE_LISTENER_THREAD = 1;
  int MAX_LISTENER_THREAD = 1;
  ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(
    CORE_LISTENER_THREAD,
    MAX_LISTENER_THREAD,
    Integer.MAX_VALUE,
    TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<>()
//    new NamedThreadFactory("configListenerOperate", MAX_LISTENER_THREAD)
  );

  void onChangeEvent(ConfigurationChangeEvent event);

  default void onProcessEvent(ConfigurationChangeEvent event) {
    getExecutorService().submit(() -> {
      beforeEvent();
      onChangeEvent(event);
      afterEvent();
    });
  }

  default void onShutDown() {
    getExecutorService().shutdownNow();
  }

  default ExecutorService getExecutorService() {
    return EXECUTOR_SERVICE;
  }

  default void beforeEvent() {

  }

  default void afterEvent() {

  }
}
