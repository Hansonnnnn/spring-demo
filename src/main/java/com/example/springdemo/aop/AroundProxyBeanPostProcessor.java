package com.example.springdemo.aop;

import org.springframework.aop.framework.AopConfigException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

public class AroundProxyBeanPostProcessor implements BeanPostProcessor {
  Map<String, Object> originBeans = new HashMap<>();

  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    Class<?> beanClass = bean.getClass();
    // 检测 @Around 注解
    Around annotation = beanClass.getAnnotation(Around.class);
    if (annotation == null) {
      return bean;
    }
    String handlerName;
    try {
      handlerName = (String) annotation.annotationType().getMethod("value").invoke(annotation);
      Object proxy = createProxy(beanClass, bean, handlerName);
      originBeans.put(beanName, bean);
      return proxy;
    } catch (ReflectiveOperationException e) {
      throw new AopConfigException("", e);
    }
  }

  Object createProxy(Class<?> beanClass, Object bean, String handlerName) {
//    ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) ApplicationContextUtils.getRequiredApplicationContext();
//    BeanDefinition def = ctx.findBeanDefinition(handlerName);
//    if (def == null) {
//      throw new AopConfigException();
//    }
//    Object handlerBean = def.getInstance();
//    if (handlerBean == null) {
//      handlerBean = ctx.createBeanAsEarlySingleton(def);
//    }
//    if (handlerBean instanceof InvocationHandler handler) {
//      return ProxyResolver.getInstance().createProxy(bean, handler);
//    } else {
//      throw new AopConfigException();
//    }
    return null;
  }

//  @Override
  public Object postProcessOnSetProperty(Object bean, String beanName) {
    Object origin = this.originBeans.get(beanName);
    return origin != null ? origin : bean;
  }
}
