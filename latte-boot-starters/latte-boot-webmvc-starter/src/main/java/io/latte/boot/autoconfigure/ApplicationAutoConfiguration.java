package io.latte.boot.autoconfigure;

import io.latte.boot.mapper.MapperEntityProperties;
import io.latte.boot.security.ApplicationRedissionProperties;
import io.latte.boot.security.SecurityProperties;
import io.latte.boot.web.context.AppContext;
import io.latte.boot.web.http.message.AppMessageContext;
import io.latte.boot.web.util.BeanUtils;
import io.latte.boot.web.util.UploadUtils;
import io.latte.boot.webmvc.ApplicationExceptionProperties;
import io.latte.boot.webmvc.ApplicationProperties;
import io.latte.boot.webmvc.BaseWebMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/**
 * Application自动配置器
 *
 * @author : wugz
 * @since : 2022/11/18
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = {"io.latte.boot.webmvc"})
@EnableConfigurationProperties(value = {
    ApplicationProperties.class,
    ApplicationExceptionProperties.class,
    ApplicationRedissionProperties.class,
    SecurityProperties.class,
    MapperEntityProperties.class})
public class ApplicationAutoConfiguration {
  /**
   * 默认BeanUtils
   *
   * @return
   */
  @Bean
  public BeanUtils defaultBeanUtils() {
    return new BeanUtils();
  }

  /**
   * 系统语言国际化支持
   * 通过在BaseWebMvcConfigurer中添加语言拦截器实现(addInterceptors())
   *
   * @param messageSource
   * @return
   * @see BaseWebMvcConfigurer
   */
  @Bean
  public AppMessageContext defaultAppMessageContext(MessageSource messageSource) {
    return new AppMessageContext(messageSource);
  }

  /**
   * 默认AppContext
   *
   * @return
   */
  @Bean
  public AppContext defaultAppContext(AppMessageContext appMessageContext) {
    return new AppContext(appMessageContext);
  }

  /**
   * 默认上传工具MultipartResolver支持
   *
   * @return
   */
  @Bean
  @ConditionalOnProperty(name = "spring.servlet.multipart.enabled", havingValue = "true", matchIfMissing = true)
  public MultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
  }

  /**
   * 默认上传工具类
   *
   * @param multipartResolver
   * @param multipartProperties
   * @return
   */
  @Bean
  @ConditionalOnProperty(name = "spring.servlet.multipart.enabled", havingValue = "true", matchIfMissing = true)
  @ConditionalOnBean(value = MultipartResolver.class)
  public UploadUtils defaultUploadUtils(MultipartResolver multipartResolver, MultipartProperties multipartProperties) {
    return UploadUtils.init(multipartResolver, multipartProperties);
  }

}
