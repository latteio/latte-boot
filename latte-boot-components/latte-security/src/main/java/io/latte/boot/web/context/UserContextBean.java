package io.latte.boot.web.context;

import io.latte.boot.security.AnonymousUser;
import io.latte.boot.security.AuthenticationAttributes;
import io.latte.boot.security.AuthenticationUser;
import io.latte.boot.security.customizer.LoginAuthenticationToken;
import io.latte.boot.support.validate.Validate;
import io.latte.boot.web.http.message.AppMessageContext;
import io.latte.boot.web.http.message.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * UserContextBean
 *
 * @author : wugz
 * @since : 2021/8/31
 */
class UserContextBean implements UserContext {
  /*
   * 上下文环境变量
   */
  private final HttpServletRequest request;
  private final Map<String, Object> requestMap;
  private boolean requireAuthorized = true;
  private boolean useToken = true;
  /*
   * 上下文属性
   */
  private AuthenticationUser authenticationUser;
  private String userLang;

  /**
   * 构造函数
   *
   * @param request
   */
  UserContextBean(HttpServletRequest request) {
    this.request = request;
    this.requestMap = new HashMap<>();
    this.requireAuthorized = (Boolean) Validate.requireNonNull(this.request.getAttribute(AuthenticationAttributes.AUTH_REQUIRED));
    this.useToken = (Boolean) Validate.requireNonNull(this.request.getAttribute(AuthenticationAttributes.AUTH_USE_TOKEN));
  }

  /**
   * 构造函数
   *
   * @param requestMap
   * @param requireAuthorized
   * @param useToken
   */
  UserContextBean(
      Map<String, Object> requestMap,
      boolean requireAuthorized,
      boolean useToken) {
    this.request = null;
    this.requestMap = null != requestMap ? requestMap : new HashMap<>();
    this.requireAuthorized = requireAuthorized;
    this.useToken = useToken;
  }

  /**
   * 构造函数
   *
   * @param authenticationUser 用户信息
   * @param requireAuthorized  是否需要认证
   * @param useToken           是否使用token验证
   */
  UserContextBean(AuthenticationUser authenticationUser, boolean requireAuthorized, boolean useToken) {
    this.request = null;
    this.requestMap = new HashMap<>();
    this.requireAuthorized = requireAuthorized;
    this.useToken = useToken;

    if (null != authenticationUser) {
      Authentication authentication = new LoginAuthenticationToken(authenticationUser, Constants.PROTECTED, authenticationUser.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }

  /**
   * 分析构建上下文
   */
  public UserContextBean build() {
    /* 1. 解析用户语言, 设置当前线程消息的语言 */
    this.userLang = null != this.request
        ? this.request.getHeader("Lang")
        : (null != this.requestMap.get("Lang") ? this.requestMap.get("Lang").toString() : null);
    if (!StringUtils.hasText(this.userLang)) {
      this.userLang = Locale.SIMPLIFIED_CHINESE.toLanguageTag();
    }

    final Locale userLocale = Locale.forLanguageTag(this.userLang);
    LocaleContextHolder.setDefaultLocale(userLocale);
    LocaleContextHolder.setLocale(userLocale);
    AppMessageContext.setLocale(userLocale);

    if (this.requireAuthorized) {
      /* 2.需要认证: 重建认证用户信息 */
      /* 2.1 直接从会话中获取认证信息 */
      Authentication authentication = SecurityContextHolder.getContext()
          .getAuthentication();
      if (null != authentication
          && !(authentication instanceof AnonymousAuthenticationToken)
          && authentication.isAuthenticated()) {
        AuthenticationUser authenticationUser = (AuthenticationUser) authentication.getPrincipal();
        if (authenticationUser.isAnonymous()) {
          this.authenticationUser = AnonymousUser.INSTANCE;
        } else {
          this.authenticationUser = new AuthenticationUser(
              authenticationUser.getAppId(),
              authenticationUser.getAppClient(),
              authenticationUser.getId(),
              authenticationUser.getUsername(),
              Constants.PROTECTED,
              authenticationUser.getAuthorities(),
              authenticationUser.getPermissions(),
              authenticationUser.getOrganizations()
          );
          this.authenticationUser.setMobile(authenticationUser.getMobile());
          this.authenticationUser.setEmail(authenticationUser.getEmail());
          this.authenticationUser.setIdcard(authenticationUser.getIdcard());
          this.authenticationUser.setIsLocked(authenticationUser.getIsLocked());
          this.authenticationUser.setIsEnabled(authenticationUser.getIsEnabled());
        }
      }
    } else {
      /* 3.设置不认证: 匿名用户 */
      this.authenticationUser = AnonymousUser.INSTANCE;
    }

    return this;
  }

  /**
   * 返回当前登录用户
   *
   * @return
   */
  public AuthenticationUser getUser() {
    return authenticationUser;
  }

  /**
   * 返回当前用户语言
   *
   * @return
   */
  public String getUserLang() {
    return userLang;
  }

}
