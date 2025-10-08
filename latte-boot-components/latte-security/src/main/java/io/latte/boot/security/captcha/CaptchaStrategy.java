package io.latte.boot.security.captcha;

import io.latte.boot.web.http.login.LoginCommand;
import io.latte.boot.web.http.response.ApiResponse;

/**
 * 验证码策略
 *
 * @author : wugz
 * @since : 2022/10/21
 */
public interface CaptchaStrategy {
  /**
   * 获取验证码
   *
   * @param principal 主体
   * @return
   */
  CaptchaCode getCode(String principal) throws Exception;

  /**
   * 校验验证码
   *
   * @param cmd 登录参数
   * @return
   */
  ApiResponse<Boolean> validateCode(LoginCommand cmd);
}

