package com.example.springdemo.seata.core.store;

import lombok.Data;

import java.util.Date;

@Data
public class GlobalTransactionDO {
  private String xid;
  private Long transactionId;
  private Integer status;
  private String applicationId;
  private String transactionServiceGroup;
  private String transactionName;
  private Integer timeout;
  private Long beginTime;
  private String applicationData;
  private Date gmtCreate;
  private Date gmtModified;
}
