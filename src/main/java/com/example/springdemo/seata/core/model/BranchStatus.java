package com.example.springdemo.seata.core.model;

public enum BranchStatus {
  Unknown(0),
  Registered(1),
  PhaseOne_Done(2),
  PhaseOne_Failed(3),
  PhaseOne_Timeout(4),
  PhaseTwo_Committed(5),
  PhaseTwo_CommitFailed_Retryable(6),
  PhaseTwo_CommitFailed_Unretryable(7),
  PhaseTwo_Rollbacked(8),
  PhaseTwo_RollbackFailed_Retryable(9),
  PhaseTwo_RollbackFailed_Unretryable(10),
  PhaseTwo_CommitFailed_XAER_NOTA_Retryable(11),
  PhaseTwo_RollbackFailed_XARE_NOTA_Retryable(12);

  private int code;

  BranchStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public static BranchStatus get(byte code) {
    return get((int) code);
  }

  public static BranchStatus get(int code) {
    BranchStatus status;
      try {
        status = BranchStatus.values()[code];
      } catch (Exception e) {
        throw new RuntimeException("Unknown BranchStatus[" + code + "]");
      }
       return status;
  }
}
