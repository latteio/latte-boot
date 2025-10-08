package io.latte.boot.mapper.provider;

import io.latte.boot.mapper.provider.impl.BaseSQLMapperProvider;
import io.latte.boot.mapper.statement.sql.SQL;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * BaseSQLMapper
 *
 * @author : wugz
 * @since : 2018/4/22
 */
public interface BaseSQLMapper {
  /**
   * 执行更新
   *
   * @param sql 查询sql
   * @return
   */
  @SelectProvider(type = BaseSQLMapperProvider.class, method = "executeSQL")
  Integer executeUpdate(SQL sql);

  /**
   * 返回列表结果
   *
   * @param sql 查询sql
   * @return
   */
  @SelectProvider(type = BaseSQLMapperProvider.class, method = "executeSQL")
  List<Map<String, Object>> executeQuery(SQL sql);
}
