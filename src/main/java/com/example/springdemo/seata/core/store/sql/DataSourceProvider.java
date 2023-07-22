package com.example.springdemo.seata.core.store.sql;

import javax.sql.DataSource;

public interface DataSourceProvider {

  DataSource provide();

}
