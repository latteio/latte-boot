package io.latte.boot.web.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 检验是否字典配置值
 *
 * @author : wugz
 * @since : 2021/12/13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = DictKeyValidator.class)
public @interface DictKey {
  /* 校验消息 */
  String message() default "{platform.validation.not-dict-key}";

  /* key集合方式: key集合 */
  String[] keys() default {};

  /* 字典方式: 字典目录code, 需要同时配置keyProvider */
  String category() default "";

  /* 字典方式: 字典数据提供者, 需要同时配置category */
  Class<? extends DictKeyProvider> keyProvider() default DictKeyProvider.class;

  /* 是否允许为空 */
  boolean nullable() default true;

  /* must contains */
  Class<?>[] groups() default {};

  /* must contains */
  Class<? extends Payload>[] payload() default {};
}
