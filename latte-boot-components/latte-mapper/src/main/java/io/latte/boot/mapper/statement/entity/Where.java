package io.latte.boot.mapper.statement.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Where
 *
 * @author : wugz
 * @since : 2018/5/11
 */
public interface Where<E> extends Serializable {
  /**
   * 获取WHERE条件集
   *
   * @return
   */
  List<WhereType> getWhereExpressions();

  /**
   * 设置WHERE条件
   *
   * @param whereExpression WHERE表达式
   */
  void whereExpression(WhereType whereExpression);
}
