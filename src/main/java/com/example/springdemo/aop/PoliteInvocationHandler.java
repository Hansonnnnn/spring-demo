package com.example.springdemo.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PoliteInvocationHandler implements InvocationHandler {
  @Override
  public Object invoke(Object bean, Method method, Object[] args) throws Throwable {
    // 修改标记了 @Polite 方法的返回值
    if (method.isAnnotationPresent(Polite.class)) {
      String ret = (String) method.invoke(bean, args);
      if (ret.endsWith(".")) {
        ret = ret.substring(0, ret.length() - 1) + "!";
      }
      return ret;
    }
    return method.invoke(bean, args);
  }
}
