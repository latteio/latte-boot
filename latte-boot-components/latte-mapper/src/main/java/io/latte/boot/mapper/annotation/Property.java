package io.latte.boot.mapper.annotation;

import java.lang.annotation.*;

/**
 * Property
 *
 * @author : wugz
 * @since : 2018/5/1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {
  /**
   * 对应字段
   *
   * @return
   */
  String field() default "";

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