package io.latte.boot.autoconfigure.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

/**
 * MethodSecurityExpressionResolver
 *
 * @author : wugz
 * @since : 2024/3/1
 */
class MethodSecurityExpressionResolver extends SecurityExpressionRoot {
  public MethodSecurityExpressionResolver(Authentication authentication) {
    super(authentication);
  }

  public MethodSecurityExpressionResolver(Supplier<Authentication> authentication) {
    super(authentication);
  }
}
