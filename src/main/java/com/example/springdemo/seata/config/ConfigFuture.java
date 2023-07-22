package com.example.springdemo.seata.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ConfigFuture {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFuture.class);
  private static final long DEFAULT_CONFIG_TIMEOUT = 5 * 1000;
  private long timeoutMills;
  private long start = System.currentTimeMillis();
  private String dataId;
  private String content;
  private ConfigOperation operation;
  private transient CompletableFuture<Object> origin = new CompletableFuture<>();

  public ConfigFuture(String dataId, String content, ConfigOperation operation) {
    this(dataId, content, operation, DEFAULT_CONFIG_TIMEOUT);
  }

  public ConfigFuture(String dataId, String content, ConfigOperation operation, long timeoutMills) {
    this.dataId = dataId;
    this.content = content;
    this.operation = operation;
    this.timeoutMills = timeoutMills;
  }

  public boolean isTimeout() {
    return System.currentTimeMillis() - start > timeoutMills;
  }

  public Object get() {
    return get(this.timeoutMills, TimeUnit.MILLISECONDS);
  }

  public Object get(long timeout, TimeUnit timeUnit) {
    this.timeoutMills = timeUnit.toMillis(timeout);
    Object result;
    try {
      result = origin.get(timeout, timeUnit);
    } catch (ExecutionException | TimeoutException | InterruptedException e) {
      //
      throw new RuntimeException(e);
    }
    if (operation == ConfigOperation.GET) {
      return result == null ? content : result;
    } else {
      return result == null ? Boolean.FALSE : result;
    }
  }

  private Object getFailResult() {
    if (operation == ConfigOperation.GET) {
      return content;
    } else {
      return Boolean.FALSE;
    }
  }

  /**
   * Sets result.
   *
   * @param result the result
   */
  public void setResult(Object result) {
    origin.complete(result);
  }

  /**
   * Gets data id.
   *
   * @return the data id
   */
  public String getDataId() {
    return dataId;
  }

  /**
   * Sets data id.
   *
   * @param dataId the data id
   */
  public void setDataId(String dataId) {
    this.dataId = dataId;
  }

  /**
   * Gets content.
   *
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * Sets content.
   *
   * @param content the content
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Gets operation.
   *
   * @return the operation
   */
  public ConfigOperation getOperation() {
    return operation;
  }

  /**
   * Sets operation.
   *
   * @param operation the operation
   */
  public void setOperation(ConfigOperation operation) {
    this.operation = operation;
  }

  /**
   * The enum Config operation.
   */
  public enum ConfigOperation {
    /**
     * Get config operation.
     */
    GET,
    /**
     * Put config operation.
     */
    PUT,
    /**
     * Putifabsent config operation.
     */
    PUTIFABSENT,
    /**
     * Remove config operation.
     */
    REMOVE
  }

}
