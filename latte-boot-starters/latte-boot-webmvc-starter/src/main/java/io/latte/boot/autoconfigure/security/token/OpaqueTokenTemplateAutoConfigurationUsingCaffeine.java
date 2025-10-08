package io.latte.boot.autoconfigure.security.token;

import io.latte.boot.security.SecurityProperties;
import io.latte.boot.security.annotation.ConditionalOnUsingCaffeine;
import io.latte.boot.security.token.OpaqueTokenTemplate;
import io.latte.boot.security.token.provider.OpaqueTokenTemplateProviderUsingCaffeine;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpaqueTokenTemplateAutoConfigurationUsingCaffeine
 *
 * @author : wugz
 * @since : 2024/9/28
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnUsingCaffeine
@EnableConfigurationProperties(value = {SecurityProperties.class})
public class OpaqueTokenTemplateAutoConfigurationUsingCaffeine {

  @Bean(name = "opaqueTokenTemplate")
  public OpaqueTokenTemplate opaqueTokenTemplateOnExistCaffeine(SecurityProperties securityProperties) {
    return new OpaqueTokenTemplateProviderUsingCaffeine(securityProperties.getAuth());
  }
}
