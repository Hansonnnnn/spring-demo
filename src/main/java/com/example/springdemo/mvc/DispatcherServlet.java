package com.example.springdemo.mvc;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过反射拿到一组 Dispatcher 对象，在 doGet 和 doPost 中依次匹配 URL
 */
public class DispatcherServlet extends HttpServlet {
  // 这里不能用Map<String, Dispatcher>的原因在于我们要处理类似/hello/{name}这样的URL，没法使用精确查找，只能使用正则匹配。
  List<Dispatcher> getDispatchers = new ArrayList<>();
  List<Dispatcher> postDispatchers = new ArrayList<>();

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String url = request.getRequestURI();
    for (Dispatcher dispatcher : getDispatchers) {
      Result result = dispatcher.process(url, request, response);
      // 匹配成功且处理后
      if (result.processed()) {
        // 处理结果
        // ...
        return;
      }
    }
    response.sendError(404, "Not Found");
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {

  }
}
