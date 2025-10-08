package io.latte.boot.mapper.statement.sql.impl;

import io.latte.boot.mapper.statement.sql.SQL;
import io.latte.boot.support.web.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * SQLImpl
 *
 * @author : wugz
 * @since : 2019/2/3
 */
public class SQLImpl extends HashMap<String, Object> implements SQL {
  private String sql;

  /**
   * 构造函数
   *
   * @param sql
   * @param params
   */
  public SQLImpl(String sql, Object params) {
    this.sql = sql;
    this.params(params);
  }

  /**
   * 构造函数
   *
   * @param sql
   */
  public SQLImpl(String sql) {
    this(sql, null);
  }

  public String getSQL() {
    return sql;
  }

  protected void params(Object params) {
    if (null != params) {
      Map<String, Object> paramsMap = MapUtils.from(params);
      this.putAll(paramsMap);
    }
  }

}
