package com.example.springdemo.seata.core.store;

public interface DistributedLocker {
  boolean acquireLock(DistributedLockDO distributedLockDO);
  boolean releaseLock(DistributedLockDO distributedLockDO);
}
