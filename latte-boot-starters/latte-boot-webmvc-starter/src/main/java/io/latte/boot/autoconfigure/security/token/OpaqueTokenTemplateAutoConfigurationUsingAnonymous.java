package io.latte.boot.autoconfigure.security.token;

import io.latte.boot.security.SecurityProperties;
import io.latte.boot.security.annotation.ConditionalOnUsingAnonymous;
import io.latte.boot.security.token.OpaqueTokenTemplate;
import io.latte.boot.security.token.provider.OpaqueTokenTemplateProviderUsingAnonymous;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpaqueTokenTemplateAutoConfigurationUsingAnonymous
 *
 * @author : wugz
 * @since : 2024/9/28
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnUsingAnonymous
@EnableConfigurationProperties(value = {SecurityProperties.class})
public class OpaqueTokenTemplateAutoConfigurationUsingAnonymous {

  @Bean(name = "opaqueTokenTemplate")
  public OpaqueTokenTemplate opaqueTokenTemplateOnExistCaffeine(SecurityProperties securityProperties) {
    return new OpaqueTokenTemplateProviderUsingAnonymous(securityProperties.getAuth());
  }
}
