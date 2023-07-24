package com.example.springdemo.tcc;

import com.example.springdemo.trans.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://xie.infoq.cn/article/e6539ce436294828b5c9420f9
 */
@Service
@Slf4j
public class TccClientService {

  private final TransactionInfoMapper transactionInfoMapper;
  public boolean transactionHandle(String xid) {
    // 根据 xid 查询所有事务分支信息
    Map<String, Object> condition = new HashMap<>(1);
    condition.put("xid", xid);

    List<Map<String, Object>> branchTransactions = transactionInfoMapper.query(condition);

    // 判断是否所有事务的 try 都执行成功了，如果都成功了，则 confirm，否则 cancel
    boolean executeConfirm = true;
    for (Map<String, Object> item : branchTransactions) {
      if (item.get("status").equals(TransactionStatus.TRY_FAILED)
      || item.get("status").equals(TransactionStatus.CONFIRM_FAILED)) {
        executeConfirm = false;
        break;
      }
    }

    if (executeConfirm) {
      return executeMethod(branchTransactions, TransactionMethod.CONFIRM);
    } else {
      return executeMethod(branchTransactions, TransactionMethod.CANCEL);
    }
  }

  /**
   * 通过分支事务注册的 类名和方法名，反射调用对应的 confirm 或者 cancel 方法
   * 可以串行，也可以并行
   * @param branchTransactions
   * @param method
   * @return
   */
  private boolean executeMethod(List<Map<String, Object>> branchTransactions, String methodName) {
    for (Map<String, Object> item : branchTransactions) {
      try {
        Class<?> clazz = Class.forName(item.get("class_name").toString());

        Method method = clazz.getDeclaredMethod(item.get(methodName).toString());

        Object service = clazz.newInstance();
        Object ret = method.invoke(service);
      }  catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    return true;
  }
}
