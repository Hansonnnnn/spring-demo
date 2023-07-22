package com.example.springdemo.jdbc;

import com.example.springdemo.trans.DataSourceTransactionManager;
import com.example.springdemo.trans.PlatformTransactionManager;
import com.example.springdemo.trans.TransactionalBeanPostProcessor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbcConfiguration {

  @Bean(destroyMethod = "close")
  public DataSource dataSource(
    @Value("${summer.datasource.url}") String url,
    @Value("${summer.datasource.username}") String username,
    @Value("${summer.datasource.password}") String password,
    @Value("${summer.datasource.driver-class-name:}") String driver,
    @Value("${summer.datasource.maximum-pool-size:20}") int maxPoolSize,
    @Value("${summer.datasource.minimum-pool-size:1}") int minPoolSize,
    @Value("${summer.datasource.connection-timeout:30000}") int connectionTimeout
  ) {
    var config = new HikariConfig();
    config.setAutoCommit(false);
    config.setJdbcUrl(url);
    config.setUsername(username);
    config.setPassword(password);
    if (StringUtils.isNotBlank(driver)) {
      config.setDriverClassName(driver);
    }
    config.setMaximumPoolSize(maxPoolSize);
    config.setMinimumIdle(minPoolSize);
    config.setConnectionTimeout(connectionTimeout);
    return new HikariDataSource(config);
  }

  @Bean
  MyJdbcTemplate myJdbcTemplate(@Autowired DataSource dataSource) {
    return new MyJdbcTemplate(dataSource);
  }

  @Bean
  TransactionalBeanPostProcessor transactionalBeanPostProcessor() {
    return new TransactionalBeanPostProcessor();
  }

  @Bean
  PlatformTransactionManager platformTransactionManager(@Autowired DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
