package com.example.springdemo.tcc;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class BranchTransactionHandler {
  private final TccClientService tccClientService;

  public BranchTransactionHandler(TccClientService tccClientService) {
    this.tccClientService = tccClientService;
  }

  @Pointcut("@annotation(xxx.xxx.xx.TccAction)")
  public void branchTransaction() {}

  /**
   * 在分支事务中执行 try 函数，拦截，将调用信息存到全局事务管理器中
   * @param point
   */
  public void branchTransactionHandler(JoinPoint point) {
    // 获取分支事务服务名
    Object target  = point.getTarget().getClass();
    String className = ((Class) target).getName();

    MethodSignature methodSignature = (MethodSignature) point.getSignature();
    Method method = methodSignature.getMethod();
    TccAction tccActionAnnotation = method.getAnnotation(TccAction.class);

    // 获取 confirm 和 cancel 对应方法名称
    String commitMethodName = tccActionAnnotation.confirmMethod();
    String cancelMethodName = tccActionAnnotation.cancelMethod();

    // 写入全局事务管理器的数据中
//    tccClientService.register(RootContext.get(), className, commitMethodName, cancelMethodName);
  }
}
