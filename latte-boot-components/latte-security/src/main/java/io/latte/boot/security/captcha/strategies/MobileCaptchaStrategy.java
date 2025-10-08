package io.latte.boot.security.captcha.strategies;

import com.github.benmanes.caffeine.cache.Cache;
import io.latte.boot.security.SecurityProperties;
import io.latte.boot.security.captcha.CaptchaCode;

import java.util.UUID;

/**
 * MobileCaptchaStrategy
 *
 * @author : wugz
 * @since : 2022/10/21
 */
public class MobileCaptchaStrategy extends UsernamePasswordCaptchaStrategy {
  /**
   * 构造函数
   */
  public MobileCaptchaStrategy(
      SecurityProperties securityProperties,
      Cache<Object, Object> securityCodeCache) {
    super(securityProperties, securityCodeCache);
  }

  /**
   * 获取验证码
   *
   * @param principal 主体
   * @return
   */
  public CaptchaCode getCode(String principal) throws Exception {
    // TODO: 生成验证码并添加到缓存
    // TODO: 通过手机短信将验证码发送给指定手机号(principal)
    return new CaptchaCode(UUID.randomUUID().toString(), null);
  }
}
