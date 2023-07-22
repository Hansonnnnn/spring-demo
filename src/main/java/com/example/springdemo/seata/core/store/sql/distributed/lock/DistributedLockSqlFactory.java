package com.example.springdemo.seata.core.store.sql.distributed.lock;

public class DistributedLockSqlFactory {

  private static final DistributedLockSql DISTRIBUTED_LOCK_SQL = new BaseDistributedLockSql();

  public static DistributedLockSql getDistributedLockSql(String dbType) {
    return DISTRIBUTED_LOCK_SQL;
  }
}
