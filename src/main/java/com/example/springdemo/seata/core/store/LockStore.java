package com.example.springdemo.seata.core.store;

import com.example.springdemo.seata.core.model.LockStatus;

import java.util.List;

public interface LockStore {
  boolean acquireLock(LockDO lockDO);
  boolean acquireLock(List<LockDO> lockDOs);
  boolean acquireLock(List<LockDO> lockDOs, boolean autoCommit, boolean skipCheckLock);
  boolean unLock(LockDO lockDO);
  boolean unLock(List<LockDO> lockDOS);
  boolean unLock(String xid);
  boolean unLock(Long branchId);
  boolean isLockable(List<LockDO> lockDOs);
  void updateLockStatus(String xid, LockStatus lockStatus);
}
