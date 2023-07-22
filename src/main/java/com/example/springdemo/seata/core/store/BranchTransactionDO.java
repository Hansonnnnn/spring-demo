package com.example.springdemo.seata.core.store;

import com.example.springdemo.seata.core.model.BranchStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BranchTransactionDO implements Comparable<BranchTransactionDO> {

  private String xid;
  private Long transactionId;
  private Long branchId;
  private String resourceGroupId;
  private String resourceId;
  private String branchType;
  private Integer status = BranchStatus.Unknown.getCode();
  private String clientId;
  private String applicationData;
  private Date gmtCreate;
  private Date gmtModified;

  @Override
  public int compareTo(BranchTransactionDO branchTransactionDO) {
    return this.getGmtCreate().compareTo(branchTransactionDO.getGmtCreate());
  }
}
