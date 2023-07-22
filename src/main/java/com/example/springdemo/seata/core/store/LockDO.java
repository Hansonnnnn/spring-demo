package com.example.springdemo.seata.core.store;

import com.example.springdemo.seata.core.model.LockStatus;
import lombok.Data;

@Data
public class LockDO {
  private String xid;
  private Long transactionId;
  private Long branchId;
  private String resourceId;
  private String tableName;
  private String pk;
  private Integer status = LockStatus.Locked.getCode();
  private String rowKey;
}
