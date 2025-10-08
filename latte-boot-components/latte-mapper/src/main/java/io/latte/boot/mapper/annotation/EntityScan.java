package io.latte.boot.mapper.annotation;

import java.lang.annotation.*;

/**
 * EntityScan
 *
 * @author : wugz
 * @since : 2021/6/15
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EntityScan {
  String[] basePackages() default {};
}
