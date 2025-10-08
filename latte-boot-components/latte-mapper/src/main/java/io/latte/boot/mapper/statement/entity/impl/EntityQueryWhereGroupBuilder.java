package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.mapper.EntityGetter;
import io.latte.boot.mapper.EntityPropertyOrMethodMeta;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.statement.entity.LogicalType;
import io.latte.boot.mapper.statement.entity.RelationalType;
import io.latte.boot.mapper.statement.entity.WhereGroup;
import io.latte.boot.mapper.statement.entity.WhereType;
import io.latte.boot.support.web.IdUtils;

/**
 * EntityQueryWhereGroupBuilder
 *
 * @author : wugz
 * @since : 2022/1/27
 */
public class EntityQueryWhereGroupBuilder<E> {
  private final EntityQuery<E> entityQuery;
  private final EntityQueryWhereBuilder<E> entityQueryWhereBuilder;
  private final WhereGroup whereGroup;
  private LogicalType logicalType;

  /**
   * 构造函数
   */
  public EntityQueryWhereGroupBuilder(EntityQuery<E> entityQuery, EntityQueryWhereBuilder<E> entityQueryWhereBuilder, WhereGroup whereGroup) {
    this.entityQuery = entityQuery;
    this.entityQuery.where().whereExpression(new WhereType(whereGroup));
    this.entityQueryWhereBuilder = entityQueryWhereBuilder;
    this.logicalType = whereGroup.getLogicalType();
    this.whereGroup = whereGroup;
  }

  /**
   * 逻辑and
   *
   * @return
   */
  public EntityQueryWhereGroupBuilder<E> and() {
    this.logicalType = LogicalType.AND;
    return this;
  }

  /**
   * 逻辑or
   *
   * @return
   */
  public EntityQueryWhereGroupBuilder<E> or() {
    this.logicalType = LogicalType.OR;
    return this;
  }

  /**
   * 结束Group返回
   *
   * @return
   */
  public EntityQueryWhereBuilder<E> endGroup() {
    return this.entityQueryWhereBuilder;
  }

  /**
   * 创建大于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> gt(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.GT, value);
  }

  /**
   * 当条件为真, 则创建大于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> gt(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.GT, value);
  }

  /**
   * 创建大于或等于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> ge(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.GE, value);
  }

  /**
   * 当条件为真, 则创建大于或等于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> ge(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.GE, value);
  }

  /**
   * 创建小于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> lt(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.LT, value);
  }

  /**
   * 当条件为真, 则创建小于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> lt(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.LT, value);
  }

  /**
   * 创建小于或等于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> le(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.LE, value);
  }

  /**
   * 当条件为真, 则创建小于或等于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> le(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.LE, value);
  }

  /**
   * 创建相等匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> eq(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.EQ, value);
  }

  /**
   * 当条件为真, 则创建相等匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> eq(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.EQ, value);
  }

  /**
   * 创建不相等匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> neq(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.NEQ, value);
  }

  /**
   * 当条件为真, 创建不相等匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> neq(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.NEQ, value);
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> like(EntityGetter<T> getter, Object value) {
    return like(true, getter, "%", value, "%");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> like(boolean condition, EntityGetter<T> getter, Object value) {
    return like(condition, getter, "%", value, "%");
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> leftLike(EntityGetter<T> getter, Object value) {
    return like(true, getter, "", value, "%");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> leftLike(boolean condition, EntityGetter<T> getter, Object value) {
    return like(condition, getter, "", value, "%");
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> rightLike(EntityGetter<T> getter, Object value) {
    return like(true, getter, "%", value, "");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityQueryWhereGroupBuilder<E> rightLike(boolean condition, EntityGetter<T> getter, Object value) {
    return like(condition, getter, "%", value, "");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param left
   * @param value
   * @param right
   * @param <T>
   * @return
   */
  private <T> EntityQueryWhereGroupBuilder<E> like(boolean condition, EntityGetter<T> getter, Object left, Object value, Object right) {
    if (condition && null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propName = entityQuery.checkParameterName(propertyMeta.getPropertyName());
        if (LogicalType.AND.equals(logicalType)) {
          whereGroup.AND().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + " like #{" + propName + "}");
        } else if (LogicalType.OR.equals(logicalType)) {
          whereGroup.OR().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + " like #{" + propName + "}");
        }
        entityQuery.put(propName, left.toString() + value.toString() + right.toString());
      }
    }
    return this;
  }

  /**
   * 创建between语句
   *
   * @param getter
   * @param start
   * @param end
   * @param <T>
   * @return
   */
  public <T> EntityQueryWhereGroupBuilder<E> between(EntityGetter<T> getter, Object start, Object end) {
    return between(true, getter, start, end);
  }

  /**
   * 如果条件为真, 则创建between语句
   *
   * @param condition
   * @param getter
   * @param start
   * @param end
   * @param <T>
   * @return
   */
  public <T> EntityQueryWhereGroupBuilder<E> between(boolean condition, EntityGetter<T> getter, Object start, Object end) {
    if (condition && null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propertyStart = propertyMeta.getPropertyName() + IdUtils.getId();
        String propertyEnd = propertyMeta.getPropertyName() + IdUtils.getId();
        if (LogicalType.AND.equals(logicalType)) {
          whereGroup.AND().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + " between #{" + propertyStart + "} and #{" + propertyEnd + "}");
        } else if (LogicalType.OR.equals(logicalType)) {
          whereGroup.OR().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + " between #{" + propertyStart + "} and #{" + propertyEnd + "}");
        }
        entityQuery.put(propertyStart, start);
        entityQuery.put(propertyEnd, end);
      }
    }
    return this;
  }

  /**
   * 属性值为空
   *
   * @param getter
   * @param <T>
   * @return
   */
  public <T> EntityQueryWhereGroupBuilder<E> isNull(EntityGetter<T> getter) {
    return isNullOrNotNull(true, getter, RelationalType.IS_NULL);
  }

  /**
   * 属性值为空
   *
   * @param condition
   * @param getter
   * @param <T>
   * @return
   */
  public <T> EntityQueryWhereGroupBuilder<E> isNull(boolean condition, EntityGetter<T> getter) {
    return isNullOrNotNull(condition, getter, RelationalType.IS_NULL);
  }

  /**
   * 属性值不为空
   *
   * @param getter
   * @param <T>
   * @return
   */
  public <T> EntityQueryWhereGroupBuilder<E> isNotNull(EntityGetter<T> getter) {
    return isNullOrNotNull(true, getter, RelationalType.IS_NOT_NULL);
  }

  /**
   * 属性值不为空
   *
   * @param condition
   * @param getter
   * @param <T>
   * @return
   */
  public <T> EntityQueryWhereGroupBuilder<E> isNotNull(boolean condition, EntityGetter<T> getter) {
    return isNullOrNotNull(condition, getter, RelationalType.IS_NOT_NULL);
  }

  /**
   * 属性值为空或不为空
   *
   * @param condition
   * @param getter
   * @param relationalType
   * @param <T>
   * @return
   */
  private <T> EntityQueryWhereGroupBuilder<E> isNullOrNotNull(boolean condition, EntityGetter<T> getter, RelationalType relationalType) {
    if (condition && null != getter && null != relationalType) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        if (LogicalType.AND.equals(logicalType)) {
          whereGroup.AND().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + relationalType.getText());
        } else if (LogicalType.OR.equals(logicalType)) {
          whereGroup.OR().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + relationalType.getText());
        }
      }
    }
    return this;
  }

  /**
   * 关系表达式
   *
   * @param condition
   * @param getter
   * @param value
   */
  private <T> EntityQueryWhereGroupBuilder<E> relationalExpression(boolean condition, EntityGetter<T> getter, RelationalType relationalType, Object value) {
    if (condition && null != getter && null != relationalType) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propName = entityQuery.checkParameterName(propertyMeta.getPropertyName());
        if (LogicalType.AND.equals(logicalType)) {
          whereGroup.AND().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + relationalType.getText() + "#{" + propName + "}");
        } else if (LogicalType.OR.equals(logicalType)) {
          whereGroup.OR().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + relationalType.getText() + "#{" + propName + "}");
        }
        entityQuery.put(propName, value);
      }
    }
    return this;
  }

}
