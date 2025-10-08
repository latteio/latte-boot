package io.latte.boot.web.annotation.http;

import java.lang.annotation.*;

/**
 * Authorized
 *
 * @author : wugz
 * @since : 2021/11/4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authorized {

  /**
   * 是否需要认证授权
   *
   * @return
   */
  boolean required() default false;
}
