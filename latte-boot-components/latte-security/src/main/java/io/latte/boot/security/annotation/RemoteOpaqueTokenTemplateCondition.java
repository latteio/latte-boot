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
public class RemoteOpaqueTokenTemplateCondition implements Condition {
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Environment env = context.getEnvironment();

    boolean useAuth = env.getProperty("application.security.auth.enabled", Boolean.class, false);
    boolean useToken = env.getProperty("application.security.auth.use-token", Boolean.class, false);
    String tokenStore = env.getProperty("application.security.auth.token-store", "map");

    return useAuth
        && useToken
        && "remote".equalsIgnoreCase(tokenStore);
  }
}
