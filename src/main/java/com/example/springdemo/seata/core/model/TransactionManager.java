package com.example.springdemo.seata.core.model;

import com.example.springdemo.seata.core.exception.TransactionException;

public interface TransactionManager {
  String begin(String applicationId, String transactionServiceGroup, String name, int timeout) throws TransactionException;
  GlobalStatus commit(String xid) throws TransactionException;
  GlobalStatus rollback(String xid) throws TransactionException;
  GlobalStatus getStatus(String xid) throws TransactionException;
  GlobalStatus globalStatus(String xid, GlobalStatus globalStatus) throws TransactionException;
}
