package io.latte.boot.web.context;

import io.latte.boot.security.AuthenticationUser;

import java.io.Serializable;

/**
 * UserContext
 *
 * @author : wugz
 * @since : 2021/7/22
 */
public interface UserContext extends Serializable {
  /**
   * 返回当前登录用户
   *
   * @return
   */
  AuthenticationUser getUser();

  /**
   * 返回当前用户语言
   *
   * @return
   */
  String getUserLang();

}
