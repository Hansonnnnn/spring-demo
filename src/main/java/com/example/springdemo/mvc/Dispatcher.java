package com.example.springdemo.mvc;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class Dispatcher {
  // 是否返回 rest
  boolean isRest;
  // 是否有@ResponseBody
  boolean isResponseBody;
  // 是否返回 void
  boolean isVoid;
  // url正则匹配
  Pattern urlPattern;
  // bean实例
  Object controller;
  // 处理方法
  Method handlerMethod;
  // 方法参数
  Param[] methodParameters;

  public Result process(String url, HttpServletRequest request, HttpServletResponse response) {
    return new Result();
  }
}
