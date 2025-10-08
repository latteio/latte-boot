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
   * 是否可调用的
   *
   * @return true / false
   */
  boolean callable() default true;
}
