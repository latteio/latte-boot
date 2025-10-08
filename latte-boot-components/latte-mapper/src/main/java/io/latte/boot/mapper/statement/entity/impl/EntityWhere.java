package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.mapper.statement.entity.Where;
import io.latte.boot.mapper.statement.entity.WhereType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * EntityWhere
 *
 * @author : wugz
 * @since : 2018/5/11
 */
public class EntityWhere<E> extends EntityMap implements Where<E> {
  private final Class<E> entityClass;
  private final EntityWhereBuilder<E> where = new EntityWhereBuilder<>(this);
  private final List<WhereType> whereExpressions = new ArrayList<>();

  /**
   * 构造函数
   *
   * @param entityClass
   */
  public EntityWhere(Class<E> entityClass) {
    this.entityClass = Objects.requireNonNull(entityClass, "entityClass is null");
  }

  protected Class<E> getEntityClass() {
    return entityClass;
  }

  /**
   * 构造where
   *
   * @return
   */
  public EntityWhereBuilder<E> where() {
    return where;
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
