package com.example.springdemo.trans;

import org.springframework.lang.Nullable;

import java.sql.Connection;

public class TransactionalUtils {

  @Nullable
  public static Connection getCurrentConnection() {
    TransactionStatus ts = DataSourceTransactionManager.transactionStatus.get();
    return ts == null ? null : ts.connection;
  }

}
