package com.example.springdemo.seata.core.store.sql.distributed.lock;

public interface DistributedLockSql {
  String getSelectDistributeForUpdateSql(String distributedLockTable);

  String getInsetSql(String distributedLockTable);

  String getUpdateSql(String distributedLockTable);

}
