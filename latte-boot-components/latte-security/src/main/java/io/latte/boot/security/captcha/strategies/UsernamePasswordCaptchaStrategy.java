package io.latte.boot.security.captcha.strategies;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.easycaptcha.Captcha;
import com.github.easycaptcha.impl.SpecCaptcha;
import io.latte.boot.security.SecurityProperties;
import io.latte.boot.security.captcha.CaptchaCode;
import io.latte.boot.security.captcha.CaptchaStrategy;
import io.latte.boot.web.http.login.LoginCommand;
import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.response.ApiResponse;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * 用户名+密码+验证码策略
 *
 * @author : wugz
 * @since : 2022/10/21
 */
public class UsernamePasswordCaptchaStrategy implements CaptchaStrategy {
  private final SecurityProperties securityProperties;
  private final Cache<Object, Object> securityCodeCache;

  /**
   * 构造函数
   */
  public UsernamePasswordCaptchaStrategy(
      SecurityProperties securityProperties,
      Cache<Object, Object> securityCodeCache) {
    this.securityProperties = securityProperties;
    this.securityCodeCache = securityCodeCache;
  }

  protected SecurityProperties getSecurityProperties() {
    return securityProperties;
  }

  protected Cache<Object, Object> getSecurityCodeCache() {
    return securityCodeCache;
  }

  /**
   * 获取验证码
   *
   * @param principal 主体
   * @return
   */
  public CaptchaCode getCode(String principal) throws Exception {
    Captcha defaultCaptcha = defaultCaptcha();
    String authId = UUID.randomUUID().toString();
    String captchaText = defaultCaptcha.text();
    this.securityCodeCache.put(authId, captchaText);
    /* 组装验证码图像 */
    return new CaptchaCode(authId, defaultCaptcha.toBase64());
  }

  /**
   * 校验验证码
   *
   * @param cmd 登录参数
   * @return
   */
  public ApiResponse<Boolean> validateCode(LoginCommand cmd) {
    /*
     * check captcha cache: <K, V> = <authId, status>
     */
    if (this.securityProperties.getLogin().isUseCode()) {
      if (!StringUtils.hasText(cmd.getAuthId())) {
        return ApiResponse.failure(MessageCodec.AUTH_ID_EMPTY);
      }

      /* Is authId in cache key */
      if (Objects.isNull(securityCodeCache.getIfPresent(cmd.getAuthId()))) {
        return ApiResponse.failure(MessageCodec.AUTH_ID_INVALID);
      }

      if (!StringUtils.hasText(cmd.getCode())) {
        return ApiResponse.failure(MessageCodec.CODE_EMPTY);
      }

      /* Is status in cache val? */
      Object cachedCode = securityCodeCache.getIfPresent(cmd.getAuthId());
      if (null == cachedCode || !cachedCode.toString().equalsIgnoreCase(cmd.getCode())) {
        return ApiResponse.failure(MessageCodec.CODE_EXPIRED);
      }

      /* 校验验证码成功, 移除验证码 */
      this.securityCodeCache.invalidate(cmd.getAuthId());
    }

    return ApiResponse.success();
  }

  private SpecCaptcha defaultCaptcha() {
    SpecCaptcha captcha = new SpecCaptcha();
    captcha.setWidth(120);
    captcha.setHeight(35);
    captcha.setLen(6);
    return captcha;
  }

}

