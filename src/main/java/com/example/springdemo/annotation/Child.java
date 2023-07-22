package com.example.springdemo.annotation;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 子类  只继承了一个method方法
 *
 * @author crazy
 */
public class Child extends Parent {

  @Override
  public void method() {
  }

  public static void main(String[] args) throws NoSuchMethodException, NoSuchFieldException {
    Class<Child> clazz = Child.class;

    System.out.println("对类进行测试");
    if (clazz.isAnnotationPresent(InheritedTest.class)) {
      System.out.println(clazz.getAnnotation(InheritedTest.class).value());
    }
    if (clazz.isAnnotationPresent(InheritedTest2.class)) {
      System.out.println(clazz.getAnnotation(InheritedTest2.class).value());
    }
    System.out.println();
    // 对方法进行测试
    System.out.println("对方法进行测试");
    Method method = clazz.getMethod("method", null);
    if (method.isAnnotationPresent(InheritedTest.class)) {
      System.out.println(method.getAnnotation(InheritedTest.class).value());
    }
    if (method.isAnnotationPresent(InheritedTest2.class)) {
      System.out.println(method.getAnnotation(InheritedTest2.class).value());
    }
    System.out.println();
    //对方法2 进行测试
    System.out.println("对方法2进行测试");
    Method method2 = clazz.getMethod("method2", null);
    if(method2.isAnnotationPresent(InheritedTest.class)){
      System.out.println(method2.getAnnotation(InheritedTest.class).value());
    }
    if(method2.isAnnotationPresent(InheritedTest2.class)){
      System.out.println(method2.getAnnotation(InheritedTest2.class).value());
    }
    System.out.println();

    System.out.println("对属性进行测试");
    Field field = clazz.getField("a");
    if (field.isAnnotationPresent(InheritedTest.class)) {
      System.out.println(field.getAnnotation(InheritedTest.class).value());
    }
    if (field.isAnnotationPresent(InheritedTest2.class)) {
      System.out.println(field.getAnnotation(InheritedTest2.class).value());
    }

  }
}

