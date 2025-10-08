package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.mapper.GenericPlaceholderMatcher;
import io.latte.boot.mapper.statement.entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * EntityQueryWhere
 *
 * @author : wugz
 * @since : 2024/10/18
 */
public class EntityQueryWhere<E> implements QueryWhere<E> {
  private final EntityQuery<E> entityQuery;
  private final List<WhereType> whereExpressions = new ArrayList<>();

  /**
   * 构造函数
   *
   * @param entityQuery
   */
  public EntityQueryWhere(EntityQuery<E> entityQuery) {
    this.entityQuery = entityQuery;
  }

  /**
   * and 条件
   *
   * @return
   */
  public QueryWhereBuilder<E> and() {
    return new EntityQueryWhereBuilder<>(entityQuery, this, LogicalType.AND);
  }

  public QueryWhere<E> and(String expression, Object value) {
    final String whereExpressionsNew = GenericPlaceholderMatcher.matchAndReplace(expression);
    entityQuery.where().whereExpression(new WhereType(LogicalType.AND, whereExpressionsNew));
    entityQuery.bindingParameters(whereExpressionsNew, value);

    return this;
  }

  /**
   * or 条件
   *
   * @return
   */
  public QueryWhereBuilder<E> or() {
    return new EntityQueryWhereBuilder<>(entityQuery, this, LogicalType.OR);
  }

  public QueryWhere<E> or(String expression, Object value) {
    final String whereExpressionsNew = GenericPlaceholderMatcher.matchAndReplace(expression);
    entityQuery.where().whereExpression(new WhereType(LogicalType.OR, whereExpressionsNew));
    entityQuery.bindingParameters(whereExpressionsNew, value);

    return this;
  }

  /**
   * endWhere
   *
   * @return
   */
  public QuerySelection<E> endWhere() {
    return new EntityQuerySelection<>(entityQuery);
  }

  /**
   * 获取WHERE条件集
   */
  public List<WhereType> getWhereExpressions() {
    return whereExpressions;
  }

  /**
   * 设置WHERE条件
   *
   * @param whereExpression WHERE表达式
   */
  public void whereExpression(WhereType whereExpression) {
    this.whereExpressions.add(whereExpression);
  }
}
