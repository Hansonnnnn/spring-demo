package com.example.springdemo.seata.core.model;

public enum GlobalStatus {
  Unknown(0, "an ambiguous transaction state, usually use before begin"),
  Begin(1, "global transaction start"),
  Committing(2, "2Phase committing"),
  CommitRetrying(3, "2Phase committing failure retry"),
  Rollbacking(4, "2Phase rollbacking"),
  RollbackRetrying(5, "2Phase rollback failure retry"),
  TimeoutRollbacking(6, "after global transaction timeout rollbacking"),
  TimeoutRollbackRetrying(7, "after global transaction timeout rollback retrying"),
  AsyncCommitting(8, "2Phase committing, used for AT mode"),
  Committed(9, "global transaction completed with status committed"),
  CommitFailed(10, "2Phase commit failed"),
  Rollbacked(11, "global transaction completed with status rollbacked"),
  RollbackFailed(12, "global transaction completed bue rollback failed"),
  TimeoutRollbacked(13, "global transaction completed with rollback due to timeout"),
  TimeoutRollbackFailed(14, "global transaction was rollbacking due to timeout, but failed"),
  Finished(15, "ambiguous transaction status for not-exist transaction and global report for Saga"),
  CommitRetryTimeout(16, "global transaction still failed after commit failure and retries for some time"),
  RollbackRetryTimeout(17, "global transaction still failed after commit failure and retries for some time")
  ;


  private final int code;
  private final String desc;

  GlobalStatus(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  /**
   * Gets code.
   *
   * @return the code
   */
  public int getCode() {
    return code;
  }

  /**
   * Get global status.
   *
   * @param code the code
   * @return the global status
   */
  public static GlobalStatus get(byte code) {
    return get((int)code);
  }

  /**
   * Get global status.
   *
   * @param code the code
   * @return the global status
   */
  public static GlobalStatus get(int code) {
    GlobalStatus value = null;
    try {
      value = GlobalStatus.values()[code];
    } catch (Exception e) {
      throw new IllegalArgumentException("Unknown GlobalStatus[" + code + "]");
    }
    return value;
  }

  public static boolean isOnePhaseTimeout(GlobalStatus status) {
    if (status == TimeoutRollbacking
    || status == TimeoutRollbackRetrying
    || status == TimeoutRollbacked
    || status == TimeoutRollbackFailed
    ) {
      return true;
    }
    return false;
  }

  public static boolean isTwoPhaseSuccess(GlobalStatus status) {
    if (status == GlobalStatus.Committed
    || status == GlobalStatus.Rollbacked
    || status == GlobalStatus.TimeoutRollbacked) {
      return true;
    }
    return false;
  }

  public static boolean isTwoPhaseHeuristic(GlobalStatus status) {
    return status == GlobalStatus.Finished;
  }

}
