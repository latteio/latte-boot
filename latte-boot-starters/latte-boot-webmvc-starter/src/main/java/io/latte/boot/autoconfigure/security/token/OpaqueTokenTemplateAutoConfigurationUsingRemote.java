package io.latte.boot.autoconfigure.security.token;

import io.latte.boot.security.SecurityProperties;
import io.latte.boot.security.annotation.ConditionalOnUsingRemote;
import io.latte.boot.security.token.OpaqueTokenTemplate;
import io.latte.boot.security.token.provider.OpaqueTokenTemplateProviderUsingRemote;
import io.latte.boot.security.token.provider.OpaqueTokenTemplateRemoteProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpaqueTokenTemplateAutoConfigurationUsingRemote
 *
 * @author : wugz
 * @since : 2024/9/28
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnUsingRemote
@EnableConfigurationProperties(value = {SecurityProperties.class})
public class OpaqueTokenTemplateAutoConfigurationUsingRemote {

  @Bean(name = "opaqueTokenTemplate")
  public OpaqueTokenTemplate opaqueTokenTemplateOnExistRemote(SecurityProperties securityProperties, OpaqueTokenTemplateRemoteProvider opaqueTokenTemplateRemoteProvider) {
    return new OpaqueTokenTemplateProviderUsingRemote(securityProperties.getAuth(), opaqueTokenTemplateRemoteProvider);
  }
}
