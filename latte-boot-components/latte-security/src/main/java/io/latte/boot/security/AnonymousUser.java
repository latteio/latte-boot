package io.latte.boot.security;

import io.latte.boot.web.http.message.Constants;

import java.util.HashSet;
import java.util.Set;

/**
 * 匿名用户
 *
 * @author : wugz
 * @since : 2022/1/27
 */
public interface AnonymousUser {
  AuthenticationUser INSTANCE = getUserInternal();

  private static AuthenticationUser getUserInternal() {
    String ID = "0";
    String USERNAME = "anonymousUser";
    String PASSWORD = Constants.PROTECTED;
    String ROLE_TYPE = "ROLE_ANONYMOUS";
    String PERMISSION_TYPE = "PERM_ANONYMOUS";
    String MOBILE = Constants.ANONYMOUS;
    String EMAIL = Constants.ANONYMOUS;
    String IDCARD = Constants.ANONYMOUS;
    Integer ENABLED = 1;
    Integer LOCKED = 0;
    String APP_ID = Constants.ANONYMOUS;
    String APP_CLIENT = Constants.ANONYMOUS;

    Set<UserAuthority> ROLE_AUTHORITIES = new HashSet<>() {{
      add(new UserAuthority(ROLE_TYPE));
    }};

    Set<UserAuthority> PERM_AUTHORITIES = new HashSet<>() {{
      add(new UserAuthority(PERMISSION_TYPE));
    }};

    Set<UserOrganization> ORGS_AUTHORITIES = new HashSet<>() {{
      add(new AnonymousUserOrganization());
    }};

    AuthenticationUser authenticationUser = new AuthenticationUser(
        APP_ID,
        APP_CLIENT,
        ID,
        USERNAME,
        PASSWORD,
        ROLE_AUTHORITIES,
        PERM_AUTHORITIES,
        ORGS_AUTHORITIES,
        true
    );
    authenticationUser.setMobile(MOBILE);
    authenticationUser.setEmail(EMAIL);
    authenticationUser.setIdcard(IDCARD);
    authenticationUser.setIsLocked(LOCKED);
    authenticationUser.setIsEnabled(ENABLED);
    return authenticationUser;
  }
}
