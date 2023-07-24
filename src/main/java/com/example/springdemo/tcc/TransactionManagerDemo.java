package com.example.springdemo.tcc;

/**
 * 1. 初始化：向事务管理器注册新事务，生成全局事务唯一 ID （放入 threadLocal）
 * 2. try：try 阶段注册调用记录，发送 try 执行结果到事务管理器，事务管理器根据执行结果执行 confirm 或者 cancel 操作
 * 3. confirm：事务管理器根据 try 结果自动调用
 * 4. cancel：同 confirm
 */
public class TransactionManagerDemo {

  @TccGlobalTransaction
  public String buy() {
//    if (!userService.buy()) {
//      throw errror;
//    }
//    if (!storeService.tryDeduct()) {
//      throw error;
//    }
//    return Tcc.xid();
    return null;
  }
}
