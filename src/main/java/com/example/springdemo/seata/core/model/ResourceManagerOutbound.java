package com.example.springdemo.seata.core.model;

public interface ResourceManagerOutbound {
  Long branchRegister(
    BranchType branchType,
    String resourceId,
    String clientId,
    String xid,
    String applicationData,
    String lockKeys
  ) throws TransactionException;

  void branchReport(
    BranchType branchType, String xid, long branchId, BranchStatus status, String applicationData
  );

  boolean lockQuery(BranchType branchType, String resourceId, String xid, String lockKeys)
    throws TransactionException;
}
