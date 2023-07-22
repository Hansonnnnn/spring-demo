package com.example.springdemo.seata.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Result<T> {

  private T result;
  private String errMsg;
  private Object[] errMsgParams;

  public static Result<Boolean> ok() {
    return new Result<>(true, null, null);
  }

  public static <T> Result<T> build(T result) {
    return new Result<>(result, null, null);
  }

  public static <T> Result<T> build(T result, String errMsg) {
    return new Result<>(result, errMsg, null);
  }

  public static <T> Result<T> buildWithParams(T result, String errMsg, Object... args) {
    return new Result<>(result, errMsg, args);
  }
}
