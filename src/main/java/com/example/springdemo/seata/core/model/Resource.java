package com.example.springdemo.seata.core.model;

public interface Resource {
  String getResourceId();
  String getResourceGroupId();
  BranchType getBranchType();
}
