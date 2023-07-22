package com.example.springdemo.mvc;

public enum ParamType {
  PATH_VARIABLE, //
  REQUEST_PARAM, //
  REQUEST_BODY, //
  SERVLET_VARIABLE // HttpServletRequest等Servlet API提供的参数，直接从DispatcherServlet的方法参数获得
}
