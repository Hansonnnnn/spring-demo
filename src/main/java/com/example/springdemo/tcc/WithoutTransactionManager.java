package com.example.springdemo.tcc;

public class WithoutTransactionManager {

  public void buy() {
//    // try
//    // 用户账户扣减余额
//    if (!userService.tryDeductAccount()) {
//      throw new RuntimeException();
//    }
//    // 库存扣减
//    if (!storeService.tryDeductAccount()) {
//      // 需要将用户扣减余额操作 cancel 掉
//      userService.cancelDeductAccount();
//      throw new RuntimeException();
//    }
//    if (!userService.confirmDeductAccount() || !storeService.confirmDeductAccount()) {
//      userService.cancelDeductAccount();
//      storeService.cancelDeductAccount();
//    }
  }
}
