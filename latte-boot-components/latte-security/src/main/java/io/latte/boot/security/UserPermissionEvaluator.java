package io.latte.boot.security;

import io.latte.boot.support.validate.Validate;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 用户权限评估器
 *
 * @author : wugz
 * @since : 2021/10/18
 */
public class UserPermissionEvaluator implements PermissionEvaluator {
  private final SecurityProperties securityProperties;

  public UserPermissionEvaluator(SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  /**
   * @param authentication     represents the user in question. Should not be null.
   * @param targetDomainObject the domain object for which permissions should be
   *                           checked. May be null in which case implementations should return false, as the null
   *                           condition can be checked explicitly in the expression.
   * @param permission         a representation of the permission object as supplied by the
   *                           expression system. Not null.
   * @return true if the permission is granted, false otherwise
   */
  public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
    if (!this.securityProperties.getHttpRequest().isUsePermission()) {
      return true;
    }

    Validate.requireNonNull(authentication);
    Validate.requireNonNull(permission);

    if (authentication.isAuthenticated()) {
      AtomicBoolean valid = new AtomicBoolean(false);
      AuthenticationUser user = (AuthenticationUser) authentication.getPrincipal();
      if (null != user.getPermissions() && !user.getPermissions().isEmpty()) {
        user.getPermissions().forEach(perms -> {
          if (perms.getAuthority().equals(permission.toString())) {
            valid.set(true);
          }
        });

        return valid.get();
      }
    }

    return false;
  }

  /**
   * Alternative method for evaluating a permission where only the identifier of the
   * target object is available, rather than the target instance itself.
   *
   * @param authentication
   * @param targetId
   * @param targetType
   * @param permission
   * @return
   */
  @Deprecated
  public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
    return false;
  }
}
