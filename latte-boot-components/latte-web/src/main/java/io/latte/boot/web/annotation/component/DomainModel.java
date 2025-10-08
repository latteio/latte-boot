package io.latte.boot.web.annotation.component;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * DomainModel
 *
 * @author : wugz
 * @since : 2024/11/2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface DomainModel {
  @AliasFor(annotation = Component.class)
  String value() default "";

  /**
   * 访问前缀(映射前缀)
   *
   * @return 访问前缀(映射前缀)
   */
  String apiPrefix() default "";
}
