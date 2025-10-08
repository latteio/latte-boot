package io.latte.boot.autoconfigure.security;

import io.latte.boot.security.RequestAccessDeniedHandler;
import io.latte.boot.security.RequestAccessUnauthorizedHandler;
import io.latte.boot.webmvc.ApplicationProperties;
import io.latte.boot.webmvc.advicer.CommonExceptionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security自动配置器
 * <p>适用于前后端分离WebSecurity配置</p>
 *
 * @author : wugz
 * @since : 2022/11/18
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class WebSecurityAutoConfiguration {
  private final ApplicationProperties applicationProperties;

  /**
   * 构造函数
   *
   * @param applicationProperties 应用上下文属性集
   */
  public WebSecurityAutoConfiguration(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    // 1.异常处理: 过滤器异常全局处理; Security异常处理;
    httpSecurity.addFilterAfter(new CommonExceptionFilter(), UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            exception -> {
              exception.authenticationEntryPoint(new RequestAccessUnauthorizedHandler());
              exception.accessDeniedHandler(new RequestAccessDeniedHandler());
            }
        );

    // 2.认证:
    // 2.1 禁用默认的登录和登出
    httpSecurity.formLogin(AbstractHttpConfigurer::disable);
    httpSecurity.logout(AbstractHttpConfigurer::disable);

    // 2.2.会话校验: 放行全部请求, 通过拦截器进行过滤, 参见RequestAuthenticationInterceptor
    httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());

    // 2.3.默认使用Token存储会话: 禁用session
    httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(
        applicationProperties.getSecurity()
            .getAuth()
            .isUseToken()
            ? SessionCreationPolicy.STATELESS
            : SessionCreationPolicy.ALWAYS));

    // 3.启用缓存headers
    // 3.1.访问安全: 允许非同源访问(支持跨域); 防xss攻击; 启用cors; 禁用csrf
    httpSecurity.headers(httpSecurityHeadersConfigurer -> {
      httpSecurityHeadersConfigurer.cacheControl(cacheControlConfig -> {
      });
      httpSecurityHeadersConfigurer.frameOptions(frameOptionsConfig -> {
        try {
          frameOptionsConfig.sameOrigin().disable();
          frameOptionsConfig.sameOrigin().xssProtection(HeadersConfigurer.XXssConfig::disable).disable()
              .cors(corsConfig -> {
              })
              .csrf(AbstractHttpConfigurer::disable);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });

    });

    return httpSecurity.build();
  }
}
