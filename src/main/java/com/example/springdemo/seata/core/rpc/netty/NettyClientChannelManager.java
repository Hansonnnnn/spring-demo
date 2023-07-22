package com.example.springdemo.seata.core.rpc.netty;

import io.netty.channel.Channel;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class NettyClientChannelManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientChannelManager.class);
  private static final ConcurrentMap<String, Object> channelLocks = new ConcurrentHashMap<>();
  private static final ConcurrentMap<String, NettyPoolKey> poolKeyMap = new ConcurrentHashMap<>();
  private static final ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();
  private static final GenericKeyedObjectPool<NettyPoolKey, Channel> nettyClientKeyPool = null;
  private Function<String, NettyPoolKey> poolKeyFunction;

//  NettyClientChannelManager(final NettyPoolableFactory keyPoolableFactory,
//                            final Function<String, NettyPoolKey> poolKeyFunction,
//                            final NettyClientConfig clientConfig
//  ) {
//    nettyClientKeyPool = new GenericKeyedObjectPool<>(keyPoolableFactory);
//    nettyClientKeyPool.setConfig(clientConfig);
//    this.poolKeyFunction = poolKeyFunction;
//  }

//  private GenericKeyedObjectPool.Config getNettyPoolConfig() {
//    //
//    return null;
//  }

  ConcurrentMap<String, Channel> getChannels() {
    return channels;
  }

  Channel acquireChannel(String serverAddress) {
    Channel channelToServer = channels.get(serverAddress);
    if (channelToServer != null) {
//      channelToServer = getExistAliveChannel(channelToServer, serverAddress);
      if (channelToServer != null) {
        return channelToServer;
      }
    }
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("will connect to {}", serverAddress);
    }
//    Object lockObject = CollectionUtils.computeIfAbsent(channelLocks, serverAddress, key -> new Object());
//    synchronized (lockObject) {
//      return doConnect(serverAddress);
//    }
    return null;
  }

  void releaseChannel(Channel channel, String serverAddress) {
    if (channel == null || serverAddress == null) {
      return;
    }
    try {
      synchronized (channelLocks.get(serverAddress)) {
        Channel ch = channels.get(serverAddress);
        if (ch == null) {
          nettyClientKeyPool.returnObject(poolKeyMap.get(serverAddress), channel);
          return;
        }
        if (ch.compareTo(channel) == 0) {
          if (LOGGER.isInfoEnabled()) {
            LOGGER.info("return to pool, rm channel: {}", channel);
          }
          destroyChannel(serverAddress, channel);
        } else {
          nettyClientKeyPool.returnObject(poolKeyMap.get(serverAddress), channel);
        }
      }
    } catch (Exception e) {
      //
    }
  }

  void destroyChannel(String serverAddress, Channel channel) {
    if (channel == null) {
      return;
    }
    try {
      if (channel.equals(channels.get(serverAddress))) {
        channels.remove(serverAddress);
      }
      nettyClientKeyPool.returnObject(poolKeyMap.get(serverAddress), channel);
    } catch (Exception e) {
      //
    }
  }
  void reconnect(String transactionServiceGroup) {
    List<String> availList = null;
    try {
//      availList = getAvailServerList(transactionServiceGroup);
    } catch (Exception e) {
      //
      return;
    }
    if (CollectionUtils.isEmpty(availList)) {
//      RegistryServive registryService = RegistryFactory.getInstance();
//      String clusterName = registryService.getSeriveGroup(transactionServiceGroup);
//      if (!(registryService instanceof FileRegistryServiceImpl)) {
//
//      }
      return;
    }

    Set<String> channelAddress = new HashSet<>(availList.size());
    Map<String, Exception> failedMap = new HashMap<>();
    try {
      for (String serAddress : availList) {
        try {
          acquireChannel(serAddress);
          channelAddress.add(serAddress);
        } catch (Exception e) {
          failedMap.put(serAddress, e);
        }
      }
      if (failedMap.size() > 0) {

      }
    } finally {
      if (CollectionUtils.isEmpty(channelAddress)) {
        List<InetSocketAddress> aliveAddress = new ArrayList<>(channelAddress.size());
        for (String address : channelAddress) {
          String[] array = address.split(":");
          aliveAddress.add(new InetSocketAddress(array[0], Integer.parseInt(array[1])));
        }
//        RegistryFactory.getInstance().refreshAliveLokkup(transactionServiceGroup, aliveAddress);
      } else {
//        RegistryFactory.getInstance().refreshAliveLokkup(transactionServiceGroup, Collections.emptyList());
      }
    }
  }

  void invalidateObject(final String serverAddress, final Channel channel) throws Exception {
    nettyClientKeyPool.invalidateObject(poolKeyMap.get(serverAddress), channel);
  }

  void registerChannel(final String serverAddress, final Channel channel) {
    Channel channelToServer = channels.get(serverAddress);
    if (channelToServer != null && channelToServer.isActive()) {
      return;
    }
    channels.put(serverAddress, channel);
  }

  private Channel doConnect(final String serverAddress) {
    Channel channelToServer = channels.get(serverAddress);
    if (channelToServer != null && channelToServer.isActive()) {
      return channelToServer;
    }
    Channel channelFromPool;
    try {
      NettyPoolKey currentPoolKey = poolKeyFunction.apply(serverAddress);
//      if (currentPoolKey.getMessage() instanceof RegisterTMRequest) {
//        poolKeyMap.put(serverAddress, currentPoolKey);
//      } else {
//        NettyPoolKey previousPoolKey = poolKeyMap.putIfAbsent(serverAddress, currentPoolKey);
//        if (previousPoolKey != null && previousPoolKey.getMessage() instanceof RegisterRMRequest) {
//          RegisterRMRequest registerRMRequest = (RegisterRMRequest) currentPoolKey.getMessage();
//          ((RegisterRMRequest)previousPoolKey.getMessage()).setResourceIds(registerRMRequest.getResourceIds());
//        }
//      }
      channelFromPool = nettyClientKeyPool.borrowObject(poolKeyMap.get(serverAddress));
      channels.put(serverAddress, channelFromPool);
    } catch (Exception e) {
      throw new RuntimeException();
    }
    return channelFromPool;
  }


}
