package com.example.springdemo.aop;

import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public abstract class AnnotationProxyBeanPostProcessor<A extends Annotation> implements BeanPostProcessor {
  Map<String, Object> originBeans = new HashMap<>();
  Class<A> annotationClass;

  public AnnotationProxyBeanPostProcessor() {
//    this.annotationClass = getParameterizedType();
  }
}
