package io.latte.boot.security.annotation;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * AnonymousOpaqueTokenTemplateCondition
 *
 * @author : wugz
 * @since : 2025/9/19
 */
public class AnonymousOpaqueTokenTemplateCondition implements Condition {
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Environment env = context.getEnvironment();
    return Boolean.FALSE.equals(env.getProperty("application.security.auth.enabled", Boolean.class, false));
  }
}
