package com.example.springdemo.jdbc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(JdbcConfiguration.class)
@ComponentScan
@Configuration
public class AppConfig {
}
