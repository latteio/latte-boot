package io.latte.boot.mapper.statement.entity;

/**
 * 逻辑表达式组
 *
 * @author : wugz
 * @since : 2023/7/27
 */
public interface LogicalGroup {
  /**
   * 逻辑类型
   *
   * @return
   */
  LogicalType getLogicalType();

  /**
   * 组表达式
   *
   * @return
   */
  String getGroup();
}
