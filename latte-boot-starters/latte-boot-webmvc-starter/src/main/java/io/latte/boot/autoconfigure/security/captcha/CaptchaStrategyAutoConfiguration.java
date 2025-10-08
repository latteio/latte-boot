package io.latte.boot.autoconfigure.security.captcha;

import io.latte.boot.security.SecurityProperties;
import io.latte.boot.security.captcha.CaptchaStrategyFactory;
import io.latte.boot.security.captcha.CaptchaStrategyFactoryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Captcha Strategy自动配置器
 *
 * @author : wugz
 * @since : 2022/11/18
 */
@Configuration(proxyBeanMethods = false, enforceUniqueMethods = false)
public class CaptchaStrategyAutoConfiguration {
  @Bean
  @ConditionalOnBean(value = SecurityProperties.class)
  @ConditionalOnMissingBean(value = CaptchaStrategyFactoryCustomizer.class)
  public CaptchaStrategyFactory captchaStrategyFactory(SecurityProperties securityProperties) {
    return new CaptchaStrategyFactory(securityProperties);
  }

  @Bean
  @ConditionalOnBean(value = {SecurityProperties.class, CaptchaStrategyFactoryCustomizer.class})
  public CaptchaStrategyFactory captchaStrategyFactory(SecurityProperties securityProperties,
                                                       CaptchaStrategyFactoryCustomizer captchaStrategyFactoryCustomizer) {
    return new CaptchaStrategyFactory(securityProperties, captchaStrategyFactoryCustomizer);
  }
}
