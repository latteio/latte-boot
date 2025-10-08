package io.latte.boot.mapper.statement.sql;

/**
 * SQLProvider
 *
 * @author : wugz
 * @since : 2018/4/27
 */
public interface SQLProvider {
  /**
   * 返回sql
   *
   * @param referField 引用字段
   * @return
   */
  String sql(String referField);
}
