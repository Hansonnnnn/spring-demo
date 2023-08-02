package com.example.springdemo.tcc;

import com.example.springdemo.trans.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * 两个注解，一个全局事务注解标记入口，负责注册，一个拦截confirm和cancel
 * 有分支的概念
 */
@Aspect
@Component
@Slf4j
public class GlobalTransactionHandler {

//  private final TransactionInfoMapper transactionInfoMapper;

//  public GlobalTransactionHandler(TransactionInfoMapper transactionInfoMapper) {
//    this.transactionInfoMapper = transactionInfoMapper;
//  }

  @Pointcut("@annotation(xxx.xxx.xx.TccGlobalTransaction)")
  public void globalTransaction() {}

  @Around("globalTransaction()")
  public Object globalTransactionHandler(ProceedingJoinPoint point) throws UnknownHostException {
    // 生成全局事务 ID，并放入 ThreadLocal 中
    String transactionId = createTransactionId();
//    RootContext.set(transactionId);

    try {
      // try 阶段的执行
      point.proceed();
    } catch (Throwable throwable) {
      // try 失败后，在数据库中更新所有分支事务的状态
//      updateTransactionStatus(transactionId, TransactionStatus.TRY_FAILED);
      // 发送消息推动进入 cancel 阶段
      sendTryMessage(transactionId);
      return null;
    }

    // try 成功，在数据库中更新所有分支事务的状态
//    updateTransactionStatus(transactionId, TransactionStatus.TRY_SUCCESS);

    // 发送消息推动进入 confirm 阶段，如果 confirm 失败，则再次发送消息进入 cancel 阶段
    if (!sendTryMessage(transactionId)) {
      sendTryMessage(transactionId);
    }
    return null;
  }

  /**
   * 发送消息到 分支事务管理器（TM）
   * TM 收到消息后，查询事务数据库，根据事务状态，判断执行 confirm 还是 cancel
   * 这里使用 http，还可以使用 dubbo 之类的
   * @param transactionId
   * @return
   */
  private boolean sendTryMessage(String transactionId) {
    String[] slice = transactionId.split(":");
    String targetHost = slice[0];
    String targetPort = slice[1];

    RestTemplate restTemplate = new RestTemplate();
    String url = "http://" + targetHost + ":" + targetPort + "/tm/tryNext?xid=" + transactionId;
    Boolean response = restTemplate.getForObject(url, boolean.class, new HashMap<>(0));

    if (response == null || !response) {
      return false;
    } else {
      return true;
    }
  }

  private String createTransactionId() throws UnknownHostException {
    String localAddress = InetAddress.getLocalHost().getHostAddress();
    String timestamp = String.valueOf(System.currentTimeMillis());
    return localAddress + ":8000" + timestamp;
  }

  private void updateTransactionStatus(String xid, int status) {

  }
}
