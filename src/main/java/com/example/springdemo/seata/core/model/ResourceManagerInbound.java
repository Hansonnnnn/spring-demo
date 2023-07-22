package com.example.springdemo.seata.core.model;

import com.example.springdemo.seata.core.exception.TransactionException;

public interface ResourceManagerInbound {
  BranchStatus branchCommit(
    BranchType branchType,
    String xid,
    long branchId,
    String resourceId,
    String applicationData
  ) throws TransactionException;

  BranchStatus branchRollback(
    BranchType branchType,
    String xid,
    long branchId,
    String resourceId,
    String applicationData
  ) throws TransactionException;
}
