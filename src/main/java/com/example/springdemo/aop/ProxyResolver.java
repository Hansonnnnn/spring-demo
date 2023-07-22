package com.example.springdemo.aop;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyResolver {
  private final ByteBuddy byteBuddy = new ByteBuddy();

  /**
   * 传入原始Bean、拦截器，返回代理后的实力
   * @param bean
   * @param handler
   * @param <T>
   * @return
   */
  public <T> T createProxy(T bean, InvocationHandler handler) {
    // 目标bean的类型
    Class<?> targetClass = bean.getClass();
    // 动态创建 Proxy的Class
    Class<?> proxyClass = this.byteBuddy
      // 子类用默认无参数构造方法
      .subclass(targetClass, ConstructorStrategy.Default.DEFAULT_CONSTRUCTOR)
      // 拦截所有 public 方法
      .method(ElementMatchers.isPublic()).intercept(
        InvocationHandlerAdapter.of(
          new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
              return handler.invoke(bean, method, args);
            }
          }))
      // 生成字节码
      .make()
      // 加载字节码
      .load(targetClass.getClassLoader()).getLoaded();

    // 创建 Proxy 示例
    Object proxy;
    try {
      proxy = proxyClass.getConstructor().newInstance();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return (T) proxy;
  }

  public static void main(String[] args) {
    OriginBean originBean = new OriginBean();
    originBean.name = "Bob";
    assert "Hello, Bob.".equals(originBean.hello());

    // 创建 Proxy
    OriginBean proxy = new ProxyResolver().createProxy(originBean, new PoliteInvocationHandler());
    System.out.println(proxy.getClass().getName());
    // Proxy 名称与 originBean 不相同
    assert OriginBean.class != proxy.getClass();
    // proxy实例的name字段应为null:
    assert proxy.name == null;

    // 调用带@Polite的方法:
    assert ("Hello, Bob!".equals(proxy.hello()));
    // 调用不带@Polite的方法:
    assert ("Morning, Bob.".equals(proxy.morning()));
  }
}
