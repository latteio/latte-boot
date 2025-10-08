package io.latte.boot.webmvc.core;

import io.latte.boot.security.AuthenticationUser;
import io.latte.boot.web.context.AppContext;
import io.latte.boot.web.context.UserContext;
import io.latte.boot.web.context.WebContext;

/**
 * IContextService
 *
 * @author : wugz
 * @since : 2023/6/10
 */
public interface IContextService {
  /**
   * 返回当前用户
   *
   * @return
   */
  default AuthenticationUser getUser() {
    UserContext context = AppContext.getUserContext();
    return context.getUser();
  }

  /**
   * 返回用户上下文
   *
   * @return
   */
  default UserContext getUserContext() {
    return AppContext.getUserContext();
  }

  /**
   * 返回上下文
   *
   * @return
   */
  default WebContext getWebContext() {
    return AppContext.getWebContext();
  }
}
