package io.latte.boot.web.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 检验是否枚举配置值
 *
 * @author : wugz
 * @since : 2022/3/11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {EnumTypeValidatorForInteger.class, EnumTypeValidatorForString.class})
public @interface EnumType {
  /* 校验消息 */
  String message() default "{platform.validation.not-enum-key}";

  /* key提供者 */
  Class<? extends Enum> typeProvider() default Enum.class;

  /* 是否允许为空 */
  boolean nullable() default true;

  /* must contains */
  Class<?>[] groups() default {};

  /* must contains */
  Class<? extends Payload>[] payload() default {};
}
