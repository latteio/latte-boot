package io.latte.boot.mapper.annotation;

import java.lang.annotation.*;

/**
 * Entity
 *
 * @author : wugz
 * @since : 2018/6/15
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
  /**
   * (Optional) The schema of the table.
   *
   * @return
   */
  String schema() default "";

  /**
   * 实体对应的表
   *
   * @return
   */
  String table() default "";

  /**
   * 是否逻辑删除
   *
   * @return
   */
  boolean logicalDelete() default true;
}
