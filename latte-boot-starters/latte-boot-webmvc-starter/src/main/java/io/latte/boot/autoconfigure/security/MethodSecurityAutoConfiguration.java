package io.latte.boot.autoconfigure.security;

import io.latte.boot.security.SecurityProperties;
import io.latte.boot.security.UserPermissionEvaluator;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

/**
 * 用户权限方法级校验配置
 *
 * @author : wugz
 * @since : 2021/10/18
 */
@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class MethodSecurityAutoConfiguration extends DefaultMethodSecurityExpressionHandler {
  private final SecurityProperties securityProperties;

  /**
   * 构造函数
   */
  public MethodSecurityAutoConfiguration(SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  @Override
  public EvaluationContext createEvaluationContext(Supplier<Authentication> authentication, MethodInvocation mi) {
    StandardEvaluationContext context = (StandardEvaluationContext) super.createEvaluationContext(authentication, mi);
    MethodSecurityExpressionResolver root = new MethodSecurityExpressionResolver(authentication);
    root.setPermissionEvaluator(new UserPermissionEvaluator(this.securityProperties));
    context.setRootObject(root);
    return context;
  }

}
