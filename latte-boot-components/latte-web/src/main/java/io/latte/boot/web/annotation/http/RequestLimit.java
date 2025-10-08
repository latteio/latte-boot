package io.latte.boot.web.annotation.http;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * RequestLimit
 *
 * @author : wugz
 * @since : 2022/2/22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestLimit {
  /**
   * 校验ip(默认校验所有访问ip)
   *
   * @return
   */
  String ip() default "all";

  /**
   * 单位时间内访问频率
   *
   * @return
   */
  int frequency() default 1;

  /**
   * 单位时间
   *
   * @return
   */
  TimeUnit timeUnit() default TimeUnit.SECONDS;
}
