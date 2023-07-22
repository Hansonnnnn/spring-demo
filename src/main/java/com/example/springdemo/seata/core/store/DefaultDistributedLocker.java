package com.example.springdemo.seata.core.store;

public class DefaultDistributedLocker implements DistributedLocker {
  @Override
  public boolean acquireLock(DistributedLockDO distributedLockDO) {
    return true;
  }

  @Override
  public boolean releaseLock(DistributedLockDO distributedLockDO) {
    return true;
  }
}
