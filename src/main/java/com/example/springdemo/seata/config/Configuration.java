package com.example.springdemo.seata.config;

import io.micrometer.common.util.StringUtils;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

public interface Configuration {

  Map<String, String> ENV_MAP = System.getenv();

  short getShort(String dataId, short defaultValue, long timeoutMills);

  short getShort(String dataId, short defaultValue);

  short getShort(String dataId);

  int getInt(String dataId, int defaultValue, long timeoutMills);

  int getInt(String dataId, int defaultValue);

  int getInt(String dataId);

  /**
   * Gets long.
   *
   * @param dataId       the data id
   * @param defaultValue the default value
   * @param timeoutMills the timeout mills
   * @return the long
   */
  long getLong(String dataId, long defaultValue, long timeoutMills);

  /**
   * Gets long.
   *
   * @param dataId       the data id
   * @param defaultValue the default value
   * @return the long
   */
  long getLong(String dataId, long defaultValue);

  /**
   * Gets long.
   *
   * @param dataId the data id
   * @return the long
   */
  long getLong(String dataId);

  /**
   * Gets duration.
   *
   * @param dataId the data id
   * @return the duration
   */
  Duration getDuration(String dataId);

  /**
   * Gets duration.
   *
   * @param dataId       the data id
   * @param defaultValue the default value
   * @return the duration
   */
  Duration getDuration(String dataId, Duration defaultValue);

  /**
   * Gets duration.
   *
   * @param dataId       the data id
   * @param defaultValue the default value
   * @param timeoutMills the timeout mills
   * @return the duration
   */
  Duration getDuration(String dataId, Duration defaultValue, long timeoutMills);

  /**
   * Gets boolean.
   *
   * @param dataId       the data id
   * @param defaultValue the default value
   * @param timeoutMills the timeout mills
   * @return the boolean
   */
  boolean getBoolean(String dataId, boolean defaultValue, long timeoutMills);

  /**
   * Gets boolean.
   *
   * @param dataId       the data id
   * @param defaultValue the default value
   * @return the boolean
   */
  boolean getBoolean(String dataId, boolean defaultValue);

  /**
   * Gets boolean.
   *
   * @param dataId the data id
   * @return the boolean
   */
  boolean getBoolean(String dataId);

  /**
   * Gets config.
   *
   * @param dataId       the data id
   * @param defaultValue the default value
   * @param timeoutMills the timeout mills
   * @return the config
   */
  String getConfig(String dataId, String defaultValue, long timeoutMills);

  /**
   * Gets config.
   *
   * @param dataId       the data id
   * @param defaultValue the default value
   * @return the config
   */
  String getConfig(String dataId, String defaultValue);

  /**
   * Gets config.
   *
   * @param dataId       the data id
   * @param timeoutMills the timeout mills
   * @return the config
   */
  String getConfig(String dataId, long timeoutMills);

  /**
   * Gets config.
   *
   * @param dataId the data id
   * @return the config
   */
  String getConfig(String dataId);

  /**
   * Put config boolean.
   *
   * @param dataId       the data id
   * @param content      the content
   * @param timeoutMills the timeout mills
   * @return the boolean
   */
  boolean putConfig(String dataId, String content, long timeoutMills);

  /**
   * Get latest config.
   *
   * @param dataId       the data id
   * @param defaultValue the default value
   * @param timeoutMills the timeout mills
   * @return the Latest config
   */
  String getLatestConfig(String dataId, String defaultValue, long timeoutMills);

  /**
   * Put config boolean.
   *
   * @param dataId  the data id
   * @param content the content
   * @return the boolean
   */
  boolean putConfig(String dataId, String content);

  /**
   * Put config if absent boolean.
   *
   * @param dataId       the data id
   * @param content      the content
   * @param timeoutMills the timeout mills
   * @return the boolean
   */
  boolean putConfigIfAbsent(String dataId, String content, long timeoutMills);

  /**
   * Put config if absent boolean.
   *
   * @param dataId  the data id
   * @param content the content
   * @return the boolean
   */
  boolean putConfigIfAbsent(String dataId, String content);

  /**
   * Remove config boolean.
   *
   * @param dataId       the data id
   * @param timeoutMills the timeout mills
   * @return the boolean
   */
  boolean removeConfig(String dataId, long timeoutMills);

  /**
   * Remove config boolean.
   *
   * @param dataId the data id
   * @return the boolean
   */
  boolean removeConfig(String dataId);

  void addConfigListener(String dataId, ConfigurationChangeListener listener);

  void removeConfigListener(String dataId, ConfigurationChangeListener listener);

  Set<ConfigurationChangeListener> getConfigListener(String dataId);

  default String getConfigFormSys(String dataId) {
    if (StringUtils.isBlank(dataId)) {
      return null;
    }
    String content = ENV_MAP.get(dataId);
    if (null != content) {
      return content;
    }
    String envDataId = dataId.toUpperCase().replace(".", "_");
    content = ENV_MAP.get(envDataId);
    if (null != content) {
      return content;
    }
    return System.getProperty(dataId);
  }
}
