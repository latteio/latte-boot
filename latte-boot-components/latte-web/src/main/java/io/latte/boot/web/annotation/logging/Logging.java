package io.latte.boot.web.annotation.logging;

import java.lang.annotation.*;

/**
 * Logging
 *
 * @author : wugz
 * @since : 2020/11/20
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Logging {

  /* 日志描述 */
  String desc() default "";

  /* 需要认证的地方使用日志 */
  boolean requireAuthorized() default true;
}
