package com.example.springdemo.seata.core.model;

import java.util.Map;

public interface ResourceManager extends ResourceManagerInbound, ResourceManagerOutbound {

  void registerResource(Resource resource);

  void unregisterResource(Resource resource);

  Map<String, Resource> getManagedResources();

  BranchType getBranchType();

}
