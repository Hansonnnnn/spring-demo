package com.example.springdemo.seata.core.model;

import lombok.Data;

@Data
public class GlobalLockConfig {
  private int lockRetryInterval;
  private int lockRetryTimes;
  private LockStrategyMode lockStrategyMode;
}
