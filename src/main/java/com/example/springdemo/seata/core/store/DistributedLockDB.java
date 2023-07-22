package com.example.springdemo.seata.core.store;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DistributedLockDB {
  private String lockKey;
  private String lockValue;
  private Long expireTime;
}
