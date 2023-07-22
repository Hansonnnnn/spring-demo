package com.example.springdemo.seata.core.store;

import java.util.List;

public interface LogStore {
  GlobalTransactionDO queryGlobalTransactionDO(String xid);
  GlobalTransactionDO queryGlobalTransactionDO(long transactionId);
  List<GlobalTransactionDO> queryGlobalTransactionDO(int[] status, int limit);
  boolean updateGlobalTransactionDO(GlobalTransactionDO globalTransactionDO);
  boolean updateGlobalTransactionDO(GlobalTransactionDO globalTransactionDO, Integer expectedStatus);
  boolean deleteGlobalTransactionDO(GlobalTransactionDO globalTransactionDO);

  List<BranchTransactionDO> queryBranchTransactionDO(String xid);
  List<BranchTransactionDO> queryBranchTransactionDO(List<String> xids);
  boolean insertBranchTransactionDO(BranchTransactionDO branchTransactionDO);
  boolean updateBranchTransactionDO(BranchTransactionDO branchTransactionDO);
  boolean deleteBranchTransactionDO(BranchTransactionDO branchTransactionDO);

  long getCurrentMaxSessionId(long high, long low);
}
