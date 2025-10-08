package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.mapper.EntityGetter;
import io.latte.boot.mapper.EntityPropertyOrMethodMeta;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.statement.entity.LogicalType;
import io.latte.boot.mapper.statement.entity.RelationalType;
import io.latte.boot.mapper.statement.entity.WhereType;
import io.latte.boot.support.web.IdUtils;

import java.util.List;

/**
 * EntityWhereBuilder
 *
 * @author : wugz
 * @since : 2021/9/28
 */
public class EntityWhereBuilder<E> {
  private final EntityWhere<E> entityWhere;
  private LogicalType logicalType;

  public EntityWhereBuilder(EntityWhere<E> entityWhere) {
    this.entityWhere = entityWhere;
  }

  /**
   * 逻辑and
   *
   * @return
   */
  public EntityWhereBuilder<E> and() {
    this.logicalType = LogicalType.AND;
    return this;
  }

  /**
   * 逻辑or
   *
   * @return
   */
  public EntityWhereBuilder<E> or() {
    this.logicalType = LogicalType.OR;
    return this;
  }

  /**
   * 创建大于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> gt(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.GT, value);
  }

  /**
   * 当条件为真, 则创建大于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> gt(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.GT, value);
  }

  /**
   * 创建大于或等于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> ge(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.GE, value);
  }

  /**
   * 当条件为真, 则创建大于或等于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> ge(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.GE, value);
  }

  /**
   * 创建小于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> lt(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.LT, value);
  }

  /**
   * 当条件为真, 则创建小于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> lt(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.LT, value);
  }

  /**
   * 创建小于或等于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> le(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.LE, value);
  }

  /**
   * 当条件为真, 则创建小于或等于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> le(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.LE, value);
  }

  /**
   * 创建相等匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> eq(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.EQ, value);
  }

  /**
   * 当条件为真, 则创建相等匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> eq(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.EQ, value);
  }

  /**
   * 创建不相等匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> neq(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.NEQ, value);
  }

  /**
   * 当条件为真, 则创建不相等匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> neq(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.NEQ, value);
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> like(EntityGetter<T> getter, Object value) {
    return like(true, getter, "%", value, "%");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> like(boolean condition, EntityGetter<T> getter, Object value) {
    return like(condition, getter, "%", value, "%");
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> leftLike(EntityGetter<T> getter, Object value) {
    return like(true, getter, "", value, "%");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> leftLike(boolean condition, EntityGetter<T> getter, Object value) {
    return like(condition, getter, "", value, "%");
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> rightLike(EntityGetter<T> getter, Object value) {
    return like(true, getter, "%", value, "");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> EntityWhereBuilder<E> rightLike(boolean condition, EntityGetter<T> getter, Object value) {
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
  private <T> EntityWhereBuilder<E> like(boolean condition, EntityGetter<T> getter, Object left, Object value, Object right) {
    if (condition && null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propName = entityWhere.checkParameterName(propertyMeta.getPropertyName());
        entityWhere.whereExpression(
            new WhereType(logicalType,
                propertyMeta.getFieldName(),
                " like #{",
                propName,
                "}"));
        entityWhere.put(propName, left.toString() + value.toString() + right.toString());
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
  public <T> EntityWhereBuilder<E> between(EntityGetter<T> getter, Object start, Object end) {
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
  public <T> EntityWhereBuilder<E> between(boolean condition, EntityGetter<T> getter, Object start, Object end) {
    if (condition && null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propertyStart = propertyMeta.getPropertyName() + IdUtils.getId();
        String propertyEnd = propertyMeta.getPropertyName() + IdUtils.getId();
        entityWhere.whereExpression(
            new WhereType(logicalType,
                propertyMeta.getFieldName(),
                " between #{",
                propertyStart,
                "} and #{",
                propertyEnd
                , "}"));
        entityWhere.put(propertyStart, start);
        entityWhere.put(propertyEnd, end);
      }
    }
    return this;
  }

  /**
   * 创建between语句
   *
   * @param getter
   * @param values
   * @param <T>
   * @return
   */
  public <T> EntityWhereBuilder<E> between(EntityGetter<T> getter, List<?> values) {
    if (null != values && values.size() == 2) {
      return this.between(true, getter, values.get(0), values.get(1));
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
  public <T> EntityWhereBuilder<E> isNull(EntityGetter<T> getter) {
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
  public <T> EntityWhereBuilder<E> isNull(boolean condition, EntityGetter<T> getter) {
    return isNullOrNotNull(condition, getter, RelationalType.IS_NULL);
  }

  /**
   * 属性值不为空
   *
   * @param getter
   * @param <T>
   * @return
   */
  public <T> EntityWhereBuilder<E> isNotNull(EntityGetter<T> getter) {
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
  public <T> EntityWhereBuilder<E> isNotNull(boolean condition, EntityGetter<T> getter) {
    return isNullOrNotNull(condition, getter, RelationalType.IS_NOT_NULL);
  }

  /**
   * endWhere
   *
   * @return
   */
  public EntityWhere<E> endWhere() {
    return entityWhere;
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
  private <T> EntityWhereBuilder<E> isNullOrNotNull(boolean condition, EntityGetter<T> getter, RelationalType relationalType) {
    if (condition && null != getter && null != relationalType) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        entityWhere.whereExpression(new WhereType(logicalType, propertyMeta.getFieldName(), relationalType.getText()));
      }
    }
    return this;
  }

  /**
   * 关系表达式
   *
   * @param condition
   * @param getter
   * @param relationalType
   * @param value
   * @param <T>
   * @return
   */
  private <T> EntityWhereBuilder<E> relationalExpression(boolean condition, EntityGetter<T> getter, RelationalType relationalType, Object value) {
    if (condition && null != getter && null != relationalType) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propName = entityWhere.checkParameterName(propertyMeta.getPropertyName());
        entityWhere.whereExpression(
            new WhereType(logicalType,
                propertyMeta.getFieldName(),
                relationalType.getText(),
                "#{",
                propName,
                "}"));
        entityWhere.put(propName, value);
      }
    }
    return this;
  }
}
