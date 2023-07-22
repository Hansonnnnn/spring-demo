package com.example.springdemo.mvc;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.springframework.context.ApplicationContext;

/**
 * 监听 Servlet 容器的启动和销毁，在监听到初始化事件时，完成创建IoC和注册DispatcherServlet
 */
public class ContextLoaderListener implements ServletContextListener {
  // Servlet 容器启动时自动调用
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    // 创建 IoC 容器
    ApplicationContext applicationContext;
    // 实例化 DispatcherServlet
    DispatcherServlet dispatcherServlet = new DispatcherServlet();
    // 注册 DispatcherServlet
    ServletContext servletContext = null;
    var dispatcherReg = servletContext.addServlet("dispatcherServlet", dispatcherServlet);
    dispatcherReg.addMapping("/");
    dispatcherReg.setLoadOnStartup(0);
  }
}
