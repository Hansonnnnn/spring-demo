package com.example.springdemo.seata.core.model;

public enum BranchType {
  AT,
  TCC,
  SAGA,
  XA;

  public static BranchType get(byte ordinal) {
    return get((int) ordinal);
  }

  public static BranchType get(int ordinal) {
    for (BranchType branchType : BranchType.values()) {
      if (branchType.ordinal() == ordinal) {
        return branchType;
      }
    }
    throw new IllegalArgumentException("Unknown BranchType[" + ordinal + "]");
  }

  public static BranchType get(String name) {
    for (BranchType branchType : values()) {
      if (branchType.name().equals(name)) {
        return branchType;
      }
    }
    throw new IllegalArgumentException("Unknown BranchType[" + name + "]");
  }
}
