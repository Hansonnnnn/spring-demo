package com.example.springdemo.seata.core.model;

public enum LockStatus {
  Locked(0),
  Rollbacking(1);

  private final int code;

  LockStatus(int code) {
    this.code = code;
  }

  /**
   * Get lock status.
   *
   * @param code the code
   * @return the lock status
   */
  public static LockStatus get(byte code) {
    return get((int)code);
  }

  /**
   * Get lock status.
   *
   * @param code the code
   * @return the lock status
   */
  public static LockStatus get(int code) {
    LockStatus value;
    try {
      value = LockStatus.values()[code];
    } catch (Exception e) {
      throw new ShouldNeverHappenException("Unknown LockStatus[" + code + "]");
    }
    return value;
  }

  /**
   * Gets code.
   *
   * @return the code
   */
  public int getCode() {
    return code;
  }

}
