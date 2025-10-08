package io.latte.boot.webmvc;

import io.latte.boot.security.RequestAuthenticationInterceptor;
import io.latte.boot.security.RequestNoAuthenticationInterceptor;
import io.latte.boot.security.SecurityProperties;
import io.latte.boot.security.session.RequestSessionAuthenticationInterceptor;
import io.latte.boot.security.token.OpaqueTokenTemplate;
import io.latte.boot.webmvc.domain.DomainModelFunctionArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

/**
 * BaseWebMvcConfigurer:
 * <p>前后端分离架构直接继承此类.</p>
 * <p>继承类不加注解@EnableGlobalMethodSecurity, 在用户权限控制类MethodSecurityConfigurer中完成权限验证.</p>
 *
 * @author : wugz
 * @since : 2021/8/8
 */
public abstract class BaseWebMvcConfigurer implements WebMvcConfigurer {
  private final SecurityProperties securityProperties;
  private final OpaqueTokenTemplate opaqueTokenTemplate;
  private final Jackson2HttpMessageConverterCustomizer httpMessageConverterCustomizer;

  /**
   * 构造函数
   *
   * @param applicationProperties
   * @param opaqueTokenTemplate
   * @param httpMessageConverterCustomizer
   */
  public BaseWebMvcConfigurer(ApplicationProperties applicationProperties,
                              OpaqueTokenTemplate opaqueTokenTemplate,
                              Jackson2HttpMessageConverterCustomizer httpMessageConverterCustomizer) {
    this.securityProperties = applicationProperties.getSecurity();
    this.opaqueTokenTemplate = opaqueTokenTemplate;
    this.httpMessageConverterCustomizer = httpMessageConverterCustomizer;
  }

  /**
   * 设置静态资源处理器
   *
   * @param registry 注册表
   */
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    registry.addResourceHandler("/public/**").addResourceLocations("classpath:/public/");
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  /**
   * 允许跨域访问: 设置映射及处理
   *
   * @param registry 注册表
   */
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("*")
        .allowedHeaders("*")
        .exposedHeaders("Content-Range")
        .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "DELETE")
        .allowCredentials(true)
        .maxAge(3600);
  }

  /**
   * 允许跨域访问: Filter层处理(拦截器层处理)
   * Add Spring MVC lifecycle interceptors for pre- and post-processing of
   * controller method invocations and resource handler requests.
   * Interceptors can be registered to apply to all requests or be limited
   * to a subset of URL patterns.
   */
  public void addInterceptors(InterceptorRegistry registry) {
    /* 添加Option拦截器 */
    registry.addInterceptor(new OptionInterceptor()).addPathPatterns("/**");

    /* 添加Cors拦截器 */
    registry.addInterceptor(new CorsInterceptor()).addPathPatterns("/**");

    /* 添加认证拦截 */
    /* 是否启用token验证 */
    if (securityProperties.getAuth().isEnabled()) {
      if (securityProperties.getAuth().isUseToken()) {
        registry.addInterceptor(new RequestAuthenticationInterceptor(securityProperties, opaqueTokenTemplate))
            .addPathPatterns("/**");
      } else {
        registry.addInterceptor(new RequestSessionAuthenticationInterceptor(securityProperties, opaqueTokenTemplate))
            .addPathPatterns("/**")
            .excludePathPatterns("/static/**");
      }
    } else {
      registry.addInterceptor(new RequestNoAuthenticationInterceptor(securityProperties, opaqueTokenTemplate))
          .addPathPatterns("/**");
    }
  }

  /**
   * Add Converters and Formatters in addition to the ones registered by default.
   *
   * @param registry 注册表
   */
  public void addFormatters(FormatterRegistry registry) {
    /* 添加xss字符串过滤 */
    registry.addConverter(new XssStringConverter());
  }

  /**
   * Configure extend {@link HttpMessageConverter HttpMessageConverters} to use for reading or writing
   * to the body of the request or response.
   *
   * @param converters 转换器
   */
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    if (this.useCustomizedHttpMessageConverter()) {
      httpMessageConverterCustomizer.customize(converters);
    }
  }

  /**
   * 获取是否启用自定义MappingJackson2HttpMessageConverter
   * (子类可覆盖)
   *
   * @return true / false
   */
  protected boolean useCustomizedHttpMessageConverter() {
    return true;
  }

  /**
   * Add resolvers to support custom controller method argument types.
   * <p>This does not override the built-in support for resolving handler
   * method arguments. To customize the built-in support for argument
   * resolution, configure {@link RequestMappingHandlerAdapter} directly.
   *
   * @param resolvers initially an empty list
   */
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new DomainModelFunctionArgumentResolver(httpMessageConverterCustomizer.getObjectMapper()));
  }
}
