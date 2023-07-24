package com.example.springdemo.tcc;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TccAction {

  String name();
  String confirmMethod();
  String cancelMethod();

}
