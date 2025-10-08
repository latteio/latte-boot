package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.EntityGetter;
import io.latte.boot.mapper.EntityPropertyOrMethodMeta;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.statement.entity.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * EntityUpdate
 *
 * @author : wugz
 * @since : 2018/5/13
 */
public class EntityUpdate<E extends BaseEntity> extends EntityWhere<E> implements Update<E> {
  private final List<String> setExpressions = new ArrayList<>();

  /**
   * 构造函数
   *
   * @param entityClass
   */
  public EntityUpdate(Class<E> entityClass) {
    super(entityClass);
  }

  /**
   * 返回set表达式集
   */
  public List<String> getSetExpressions() {
    return setExpressions;
  }

  /**
   * UPDATE set
   *
   * @param getter set字段的getter方法
   * @param value  参数值
   */
  public EntityUpdate<E> set(EntityGetter<E> getter, Object value) {
    if (null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String expr = propertyMeta.getFieldName() + " = #{" + propertyMeta.getGetterName() + "}";
        setExpressions.add(expr);
        this.bindingParameters(expr, value);
      }
    }

    return this;
  }

}
