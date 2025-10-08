package io.latte.boot.autoconfigure.security.token;

import io.latte.boot.autoconfigure.redisson.RedissionAutoConfiguration;
import io.latte.boot.security.SecurityProperties;
import io.latte.boot.security.annotation.ConditionalOnUsingRedission;
import io.latte.boot.security.token.OpaqueTokenTemplate;
import io.latte.boot.security.token.provider.OpaqueTokenTemplateProviderUsingRedission;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpaqueTokenTemplateAutoConfigurationUsingRedission
 *
 * @author : wugz
 * @since : 2024/9/28
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnUsingRedission
@EnableConfigurationProperties(value = {SecurityProperties.class})
@AutoConfigureAfter(value = RedissionAutoConfiguration.class)
public class OpaqueTokenTemplateAutoConfigurationUsingRedission {

  @Bean(name = "opaqueTokenTemplate")
  @ConditionalOnBean(value = RedissonClient.class)
  public OpaqueTokenTemplate opaqueTokenTemplateOnExistRedisson(SecurityProperties securityProperties, RedissonClient redissonClient) {
    return new OpaqueTokenTemplateProviderUsingRedission(securityProperties.getAuth(), redissonClient);
  }
}
