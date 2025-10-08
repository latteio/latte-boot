package io.latte.boot.autoconfigure.redisson;

import io.latte.boot.security.ApplicationRedissionProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Redission自动配置器
 *
 * @author : wugz
 * @since : 2021/7/23
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = {ApplicationRedissionProperties.class})
public class RedissionAutoConfiguration {
  private static Logger logger = LoggerFactory.getLogger(RedissionAutoConfiguration.class);

  @Bean
  @ConditionalOnClass(value = RedissonClient.class)
  @ConditionalOnProperty(name = "application.redis.enabled", havingValue = "true")
  public RedissonClient redissonClient(ApplicationRedissionProperties redissionProperties) {
    RedissonClient redissonClient = null;
    try {
      Config config = new Config();
      String url = "redis://" + redissionProperties.getHost() + ":" + redissionProperties.getPort();
      SingleServerConfig singleServerConfig = config.useSingleServer()
          .setDatabase(redissionProperties.getDatabase())
          .setAddress(url);

      if (StringUtils.hasText(redissionProperties.getUsername())) {
        singleServerConfig.setUsername(redissionProperties.getUsername());
      }

      if (StringUtils.hasText(redissionProperties.getPassword())) {
        singleServerConfig.setPassword(redissionProperties.getPassword());
      }

      redissonClient = Redisson.create(config);
    } catch (Exception ex) {
      logger.error("Initializing RedissonClient error: {}", ex.getMessage());
    }

    return redissonClient;
  }

}
