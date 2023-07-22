package com.example.springdemo.jdbc;


import com.example.springdemo.trans.TransactionalUtils;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyJdbcTemplate {
  private final DataSource dataSource;

  public MyJdbcTemplate(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public <T> T execute(ConnectionCallback<T> action) throws SQLException {
    // 尝试获取当前事务链接
    Connection current = TransactionalUtils.getCurrentConnection();
    if (current != null) {
      return action.doInConnection(current);
    }
    // 无事务，从DataSource获取新链接
    try (Connection newConn = dataSource.getConnection()) {
      return action.doInConnection(newConn);
    } catch (SQLException e) {
       throw new RuntimeException(e); // DataAccessException
    }
  }

  public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws SQLException {
    return execute((Connection connection) -> {
      try (PreparedStatement ps = psc.createPreparedStatement(connection)) {
        return action.doInPreparedStatement(ps);
      }
    });
  }

//  public int update(String sql, Object... args) {
//    return execute(preparedStatementCreator(sql, args), (PreparedStatement ps) -> {
//      return ps.executeUpdate();
//    });
//  }

//  public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... args) {
//    return execute(preparedStatementCreator(sql, args),
//      (PreparedStatement ps) -> {
//        List<T> list = new ArrayList<>();
//        try (ResultSet rs = ps.executeQuery()) {
//          while (rs.next()) {
//            list.add(rowMapper.mapRow(rs, rs.getRow()));
//          }
//        }
//        return list;
//      });
//  }
}
