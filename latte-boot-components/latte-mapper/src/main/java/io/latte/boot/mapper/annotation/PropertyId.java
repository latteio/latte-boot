package io.latte.boot.mapper.annotation;

import io.latte.boot.mapper.EntityFields;

import java.lang.annotation.*;

/**
 * 主键
 *
 * @author : wugz
 * @since : 2024/8/18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PropertyId {
  /**
   * 对应字段
   *
   * @return
   */
  String field() default EntityFields.ID;

  /**
   * 校验
   *
   * @return
   */
  ValidateType validate() default ValidateType.IGNORED;

  /**
   * 是否可编辑字段(新增/修改)
   *
   * @return
   */
  boolean editable() default true;

  /**
   * 是否可查询字段
   *
   * @return
   */
  boolean selectable() default true;
}
