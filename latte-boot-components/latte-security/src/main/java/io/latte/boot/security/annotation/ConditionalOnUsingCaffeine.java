package io.latte.boot.security.annotation;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * ConditionalOnUsingCaffeine
 *
 * @author : wugz
 * @since : 2025/9/19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(CaffeineOpaqueTokenTemplateCondition.class)
public @interface ConditionalOnUsingCaffeine {
}
