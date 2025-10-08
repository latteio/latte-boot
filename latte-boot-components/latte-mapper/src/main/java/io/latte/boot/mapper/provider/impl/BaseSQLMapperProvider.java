package io.latte.boot.mapper.provider.impl;

import io.latte.boot.mapper.statement.sql.SQL;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.Objects;

/**
 * BaseSQLMapperProvider
 *
 * @author : wugz
 * @since : 2018/4/22
 */
public class BaseSQLMapperProvider extends BaseMapperProvider {
  /**
   * 通过SQL执行增删改查
   *
   * @param context 上下文
   * @param sql     查询语句
   * @return
   */
  public String executeSQL(ProviderContext context, SQL sql) {
    return Objects.requireNonNull(sql.getSQL(), "sql is null");
  }

}
