package com.example.springdemo.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class AfterInvocationHandlerAdapter implements InvocationHandler {
  // after允许修改方法返回值:
  public abstract Object after(Object proxy, Object returnValue, Method method, Object[] args);

  @Override
  public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object ret = method.invoke(proxy, args);
    return after(proxy, ret, method, args);
  }
}
