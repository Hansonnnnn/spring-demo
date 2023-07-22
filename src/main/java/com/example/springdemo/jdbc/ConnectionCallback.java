package com.example.springdemo.jdbc;

import org.springframework.lang.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionCallback<T> {

  @Nullable
  T doInConnection(Connection connection) throws SQLException;

}
