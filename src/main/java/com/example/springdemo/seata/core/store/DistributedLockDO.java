package com.example.springdemo.seata.core.store;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DistributedLockDO {
  private String lockKey;
  private String lockValue;
  private Long expireTime;
}
