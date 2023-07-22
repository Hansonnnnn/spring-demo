package com.example.springdemo.seata.core.store.sql.distributed.lock;

import com.example.springdemo.seata.core.constants.ServerTableColumnsName;

public class BaseDistributedLockSql implements DistributedLockSql {
  protected static final String DISTRIBUTED_LOCK_TABLE_PLACE_HOLD = " #distributed_lock_table# ";
  protected static final String ALL_COLUMNS = ServerTableColumnsName.DISTRIBUTED_LOCK_KEY + ", " +
    ServerTableColumnsName.DISTRIBUTED_LOCK_VALUE + ", " + ServerTableColumnsName.DISTRIBUTED_LOCK_EXPIRE;
  protected static final String SELECT_FOR_UPDATE_SQL = "SELECT " + ALL_COLUMNS + " FROM " + DISTRIBUTED_LOCK_TABLE_PLACE_HOLD
  + " WHERE " + ServerTableColumnsName.DISTRIBUTED_LOCK_KEY + " = ? FOR UPDATE";

  protected static final String INSERT_DISTRIBUTED_LOCK_SQL = "INSERT INTO " + DISTRIBUTED_LOCK_TABLE_PLACE_HOLD
    + "(" + ALL_COLUMNS + ") VALUES (?, ?, ?)";

  protected static final String UPDATE_DISTRIBUTED_LOCK_SQL = "UPDATE INTO " + DISTRIBUTED_LOCK_TABLE_PLACE_HOLD
    + " SET " + ServerTableColumnsName.DISTRIBUTED_LOCK_VALUE + "=?, " + ServerTableColumnsName.DISTRIBUTED_LOCK_EXPIRE +
    "=? WHERE " + ServerTableColumnsName.DISTRIBUTED_LOCK_KEY + "=?";

  @Override
  public String getSelectDistributeForUpdateSql(String distributedLockTable) {
    return SELECT_FOR_UPDATE_SQL.replace(DISTRIBUTED_LOCK_TABLE_PLACE_HOLD, distributedLockTable);
  }

  @Override
  public String getInsetSql(String distributedLockTable) {
    return INSERT_DISTRIBUTED_LOCK_SQL.replace(DISTRIBUTED_LOCK_TABLE_PLACE_HOLD, distributedLockTable);
  }

  @Override
  public String getUpdateSql(String distributedLockTable) {
    return UPDATE_DISTRIBUTED_LOCK_SQL.replace(DISTRIBUTED_LOCK_TABLE_PLACE_HOLD, distributedLockTable);
  }
}
