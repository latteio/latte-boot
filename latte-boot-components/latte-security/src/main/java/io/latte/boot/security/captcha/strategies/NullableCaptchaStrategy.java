package io.latte.boot.security.captcha.strategies;

import io.latte.boot.security.captcha.CaptchaCode;
import io.latte.boot.security.captcha.CaptchaStrategy;
import io.latte.boot.web.http.login.LoginCommand;
import io.latte.boot.web.http.response.ApiResponse;

import java.util.UUID;

/**
 * 无验证码策略
 *
 * @author : wugz
 * @since : 2022/10/21
 */
public class NullableCaptchaStrategy implements CaptchaStrategy {
  /**
   * 获取验证码
   *
   * @param principal 主体
   * @return
   */
  public CaptchaCode getCode(String principal) {
    return new CaptchaCode(UUID.randomUUID().toString(), null);
  }

  /**
   * 校验验证码
   *
   * @param cmd 登录参数
   * @return
   */
  public ApiResponse<Boolean> validateCode(LoginCommand cmd) {
    return ApiResponse.success();
  }
}

