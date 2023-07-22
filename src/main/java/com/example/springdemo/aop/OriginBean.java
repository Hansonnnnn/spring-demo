package com.example.springdemo.aop;

import org.springframework.stereotype.Component;

@Component
public class OriginBean {
  public String name;

  @Polite
  public String hello() {
    return "Hello, " + name + ".";
  }

  public String morning() {
    return "Morning, " + name + ".";
  }
}
