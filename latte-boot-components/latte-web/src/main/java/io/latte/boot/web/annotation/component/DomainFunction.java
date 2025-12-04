package io.latte.boot.web.annotation.component;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * DomainFunction
 *
 * @author : wugz
 * @since : 2024/11/2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ResponseBody
public @interface DomainFunction {
  String[] value() default {};

  RequestMethod method() default RequestMethod.POST;

  /**
   * 所需操作权限集
   *
   * @return {}
   */
  String[] permissions() default {};

  /**
   * 是否启用
   *
   * @return true / false
   */
  boolean enabled() default true;
}
