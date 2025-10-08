package io.latte.boot.security.captcha;

/**
 * CaptchaStrategyFactoryCustomizer
 *
 * @author : wugz
 * @since : 2023/3/12
 */
public interface CaptchaStrategyFactoryCustomizer {
  /**
   * 自定义
   *
   * @param captchaStrategyFactory
   */
  void customize(CaptchaStrategyFactory captchaStrategyFactory);
}
