package com.example.springdemo.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface ViewResolver {

  void init();

  void render(String viewName, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response);
}
