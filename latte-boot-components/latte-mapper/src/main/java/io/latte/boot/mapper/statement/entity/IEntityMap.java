package io.latte.boot.mapper.statement.entity;

import java.util.Map;

/**
 * IEntityMap
 *
 * @author : wugz
 * @since : 2024/10/18
 */
public interface IEntityMap {
  /**
   * 返回已绑定参数
   *
   * @return
   */
  Map<String, Object> getParameters();

  /**
   * 动态绑定表达式
   *
   * @param expressions
   * @param values
   */
  void bindingParameters(String expressions, Object... values);

  /**
   * 检查参数名称: 如果已经存在, 则生成一个新的并返回
   *
   * @param parameterName 参数名称
   */
  String checkParameterName(String parameterName);
}
