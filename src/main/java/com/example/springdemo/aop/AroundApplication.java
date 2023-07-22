package com.example.springdemo.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class AroundApplication {
  @Bean
  AroundProxyBeanPostProcessor createAroundProxyBeanPostProcessor() {
    return new AroundProxyBeanPostProcessor();
  }
}
