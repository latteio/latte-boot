package io.latte.boot.security.captcha;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.latte.boot.security.SecurityProperties;
import io.latte.boot.security.captcha.strategies.EmailCaptchaStrategy;
import io.latte.boot.security.captcha.strategies.MobileCaptchaStrategy;
import io.latte.boot.security.captcha.strategies.NullableCaptchaStrategy;
import io.latte.boot.security.captcha.strategies.UsernamePasswordCaptchaStrategy;
import io.latte.boot.support.validate.Validate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * CaptchaStrategyFactory
 *
 * @author : wugz
 * @since : 2022/10/21
 */
public class CaptchaStrategyFactory {
  private static final CaptchaStrategy NULLABLE_CAPTCHA_STRATEGY = new NullableCaptchaStrategy();
  private static final Map<String, CaptchaStrategy> CAPTCHA_STRATEGY_MAP = new ConcurrentHashMap<>();
  private final SecurityProperties securityProperties;
  private final Cache<Object, Object> securityCodeCache;

  /**
   * 构造函数
   *
   * @param securityProperties
   */
  public CaptchaStrategyFactory(SecurityProperties securityProperties) {
    this(securityProperties, new CaptchaStrategyFactoryCustomizerDefault());
  }

  /**
   * 构造函数
   *
   * @param securityProperties
   * @param captchaStrategyFactoryCustomizer
   */
  public CaptchaStrategyFactory(SecurityProperties securityProperties, CaptchaStrategyFactoryCustomizer captchaStrategyFactoryCustomizer) {
    Validate.requireNonNull(captchaStrategyFactoryCustomizer);

    this.securityProperties = securityProperties;
    this.securityCodeCache = Caffeine.newBuilder()
        .expireAfterWrite(securityProperties.getLogin().getCodeExpire(), TimeUnit.SECONDS)
        .weakValues()
        .recordStats()
        .build();
    this.registerDefaultStrategies();
    captchaStrategyFactoryCustomizer.customize(this);
  }

  /**
   * 获取策略
   *
   * @param type
   * @return
   */
  public static CaptchaStrategy getStrategy(String type) {
    for (Map.Entry<String, CaptchaStrategy> entry : CAPTCHA_STRATEGY_MAP.entrySet()) {
      if (entry.getKey().equals(type)) {
        return entry.getValue();
      }
    }
    return NULLABLE_CAPTCHA_STRATEGY;
  }

  /**
   * 注册默认策略
   */
  private void registerDefaultStrategies() {
    CAPTCHA_STRATEGY_MAP.put("00", NULLABLE_CAPTCHA_STRATEGY);
    CAPTCHA_STRATEGY_MAP.put("10", new UsernamePasswordCaptchaStrategy(securityProperties, securityCodeCache));
    CAPTCHA_STRATEGY_MAP.put("20", new MobileCaptchaStrategy(securityProperties, securityCodeCache));
    CAPTCHA_STRATEGY_MAP.put("30", new EmailCaptchaStrategy(securityProperties, securityCodeCache));
  }

  /**
   * 注册策略: 外部扩展用
   *
   * @param type            策略编码
   * @param captchaStrategy 策略类
   */
  public final void registerStrategy(String type, CaptchaStrategy captchaStrategy) {
    Validate.requireTrue(
        !Validate.isNullOrBlank(type) && !CAPTCHA_STRATEGY_MAP.containsKey(type),
        "Captcha strategy type not valid: " + type
    );

    Validate.requireNonNull(
        captchaStrategy,
        "Captcha strategy object not valid: " + captchaStrategy);

    CAPTCHA_STRATEGY_MAP.put(type, captchaStrategy);
  }

  /**
   * 默认CaptchaStrategyFactoryCustomizer
   */
  private static class CaptchaStrategyFactoryCustomizerDefault implements CaptchaStrategyFactoryCustomizer {
    public void customize(CaptchaStrategyFactory captchaStrategyFactory) {
    }
  }
}

