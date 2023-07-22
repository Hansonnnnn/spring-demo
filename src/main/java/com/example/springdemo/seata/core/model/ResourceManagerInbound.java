package com.example.springdemo.seata.core.model;

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
