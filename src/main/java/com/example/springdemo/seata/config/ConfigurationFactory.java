package com.example.springdemo.seata.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigurationFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationFactory.class);

  static {
//    initOriginConfiguration();
    load();
//    maybeNeedOriginFileInstance();
  }

  private static void load() {

  }
}
