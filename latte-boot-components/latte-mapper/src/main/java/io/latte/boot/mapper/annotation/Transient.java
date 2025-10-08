package io.latte.boot.mapper.annotation;

import java.lang.annotation.*;

/**
 * Transient
 *
 * @author : wugz
 * @since : 2018/5/1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Transient {
  /**
   * 对应字段
   *
   * @return
   */
  String field() default "";

  /**
   * 该属性通过语句获取值
   *
   * @return
   */
  String sql() default "";

}