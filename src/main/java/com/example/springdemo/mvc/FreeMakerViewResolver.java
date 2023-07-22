package com.example.springdemo.mvc;

import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.netty.handler.codec.http2.Http2FrameReader;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.server.ServerErrorException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class FreeMakerViewResolver implements ViewResolver {

  final String templatePath;
  final String templateEncoding;
  final ServletContext servletContext;

  Configuration config;

  public FreeMakerViewResolver(
    ServletContext servletContext,
    String templatePath,
    String templateEncoding
  ) {
    this.servletContext = servletContext;
    this.templatePath = templatePath;
    this.templateEncoding = templateEncoding;
  }

  @Override
  public void init() {
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
    cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
    cfg.setDefaultEncoding(this.templateEncoding);
//    cfg.setTemplateLoader(new ServletTemplateLoader(this.servletContext, this.templatePath));
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
    cfg.setAutoEscapingPolicy(Configuration.ENABLE_IF_SUPPORTED_AUTO_ESCAPING_POLICY);
    cfg.setLocalizedLookup(false);

    var ow = new DefaultObjectWrapper(Configuration.VERSION_2_3_32);
    ow.setExposeFields(true);
    cfg.setObjectWrapper(ow);
    this.config = cfg;
  }

  @Override
  public void render(String viewName, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
//    Template template = null;
//    try {
//      template = this.config.getTemplate(viewName);
//    } catch (Exception e) {
//      throw new ServerErrorException("View not found: " + viewName);
//    }
//    PrintWriter pw = response.getWriter();
//    try {
//      template.process(model, pw);
//    } catch (TemplateException | IOException e) {
//      throw new ServerErrorException(e);
//    }
//    pw.flush();
  }


}
