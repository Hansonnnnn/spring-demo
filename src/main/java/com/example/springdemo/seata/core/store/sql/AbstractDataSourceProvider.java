package com.example.springdemo.seata.core.store.sql;

import com.example.springdemo.seata.config.Configuration;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractDataSourceProvider implements DataSourceProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataSourceProvider.class);
  private DataSource dataSource;
  protected static final Configuration CONFIG = null; //  = ConfigurationFactory.getInstance()
  private static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
  private static final String MYSQL8_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
  private static final String MYSQL_DRIVER_FILE_PREFIX = "mysql-connector-java-";
  private static final Map<String, ClassLoader> MYSQL_DRIVER_LOADERS;
  static {
    MYSQL_DRIVER_LOADERS = createMysqlDriverClassLoaders();
  }

//  @Override
  public void init() {
    this.dataSource = generate();
  }

  @Override
  public DataSource provide() {
    return this.dataSource;
  }

  public abstract DataSource generate();

//  protected DBType getDBType() {
//    return DBType.valueof(CONFIG.getConfig(ConfigurationKeys.STORE_DB_TYPE));
//  }

  protected String getDriverClassName() {
//    String driverClassName = CONFIG.getConfig(ConfigurationKeys.STORE_DB_DRIVER_CLASS_NAME);
//    if (StringUtils.isBlank(driverClassName)) {
//      throw new StoreException(
//        String.format("the {%s} can't be empty", ConfigurationKeys.STORE_DB_DRIVER_CLASS_NAME)
//      );
//    }
//    return driverClassName;
    return null;
  }

  protected Long getMaxWait() {
    return null;
//    return CONFIG.getLong(ConfigurationKeys.STORE_DB_MAX_WAIT, DEFAULT_DB_MAX_WAIT);
  }

  protected ClassLoader getDriverClassLoader() {
    return MYSQL_DRIVER_LOADERS.getOrDefault(getDriverClassName(), ClassLoader.getSystemClassLoader());
  }

  private static Map<String, ClassLoader> createMysqlDriverClassLoaders() {
    Map<String, ClassLoader> loaders = new HashMap<>();
    String cp = System.getProperty("java.class.path");
    if (cp == null || cp.isEmpty()) {
      return loaders;
    }
    Stream.of(cp.split(File.pathSeparator))
      .map(File::new)
      .filter(File::exists)
      .map(file -> file.isFile() ? file.getParentFile() : file)
      .filter(Objects::nonNull)
      .filter(File::isDirectory)
      .map(file -> new File(file, "jdbc"))
      .filter(File::exists)
      .filter(File::isDirectory)
      .distinct()
      .flatMap(file -> {
        File[] files = file.listFiles((f, name) -> name.startsWith(MYSQL_DRIVER_FILE_PREFIX));
        if (files != null) {
          return Stream.of(files);
        } else {
          return Stream.of();
        }
      })
      .forEach(file -> {
        if (loaders.containsKey(MYSQL8_DRIVER_CLASS_NAME) && loaders.containsKey(MYSQL_DRIVER_CLASS_NAME)) {
          return;
        }
        try {
          URL url = file.toURI().toURL();
          ClassLoader loader = new URLClassLoader(new URL[]{url}, ClassLoader.getSystemClassLoader());
          try {
            loader.loadClass(MYSQL8_DRIVER_CLASS_NAME);
            loaders.putIfAbsent(MYSQL8_DRIVER_CLASS_NAME, loader);
          } catch (ClassNotFoundException e) {
            loaders.putIfAbsent(MYSQL_DRIVER_CLASS_NAME, loader);
          }
        } catch (MalformedURLException ignore) {
          //
        }
      });
    return loaders;
  }


}
