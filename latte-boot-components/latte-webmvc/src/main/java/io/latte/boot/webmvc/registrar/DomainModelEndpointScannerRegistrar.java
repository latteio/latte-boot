package io.latte.boot.webmvc.registrar;

import io.latte.boot.support.validate.Validate;
import io.latte.boot.support.web.PathUtils;
import io.latte.boot.web.annotation.component.DomainFunction;
import io.latte.boot.web.annotation.component.DomainModel;
import io.latte.boot.webmvc.ApplicationProperties;
import io.latte.boot.webmvc.domain.IDomainModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 通用领域模型 Endpoint 注册器
 *
 * @author : wugz
 * @since : 2025/6/7
 */
public class DomainModelEndpointScannerRegistrar implements IComponentScannerRegistrar {
  private final Logger logger = LoggerFactory.getLogger(DomainModelEndpointScannerRegistrar.class);
  private final ApplicationContext applicationContext;
  private final ApplicationProperties applicationProperties;
  private final RequestMappingHandlerMapping requestMappingHandlerMapping;

  /**
   * 构造函数
   *
   * @param applicationContext           applicationContext
   * @param applicationProperties        applicationProperties
   * @param requestMappingHandlerMapping requestMappingHandlerMapping
   */
  public DomainModelEndpointScannerRegistrar(ApplicationContext applicationContext,
                                             ApplicationProperties applicationProperties,
                                             RequestMappingHandlerMapping requestMappingHandlerMapping) {
    this.applicationContext = applicationContext;
    this.applicationProperties = applicationProperties;
    this.requestMappingHandlerMapping = requestMappingHandlerMapping;
  }

  public void scanAndRegister() {
    logger.debug("Start scanning domain models ...");

    String commonApiPrefix = StringUtils.hasText(this.applicationProperties.getSecurity().getHttpRequest().getApiPrefix())
        ? this.applicationProperties.getSecurity().getHttpRequest().getApiPrefix()
        : "/api";
    String domainModelApiPrefix;

    String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(DomainModel.class);
    for (String domainModelName : beanNamesForAnnotation) {
      /* 获取领域模型对象 */
      IDomainModel<?, ?, ?> proxyDomainObject = Validate.requireNonNull(applicationContext.getBean(domainModelName, IDomainModel.class),
          "Component named '%s' was not found or may not be subclass of '%s'",
          domainModelName,
          IDomainModel.class.getSimpleName());

      /* 获取领域模型对象(或代理对象)的类 */
      Class<?> proxyDomainClass = proxyDomainObject.getClass();
      Class<?> domainClass = AopUtils.getTargetClass(proxyDomainObject);

      /* 循环遍历公有方法(非静态的非final), 将标记有@DomainMethod注解的方法注册为 endpoint */
      domainModelApiPrefix = domainClass.getAnnotation(DomainModel.class).apiPrefix();
      for (Method proxyDomainMethod : proxyDomainClass.getMethods()) {

        Method domainMethod = null;
        try {
          domainMethod = domainClass.getMethod(proxyDomainMethod.getName(), proxyDomainMethod.getParameterTypes());
        } catch (NoSuchMethodException e) {
          continue;
        }

        if (domainMethod.isAnnotationPresent(DomainFunction.class)
            && !Modifier.isStatic(domainMethod.getModifiers())
            && !Modifier.isFinal(domainMethod.getModifiers())) {
          DomainFunction domainFunctionAnnotation = domainMethod.getAnnotation(DomainFunction.class);

          /* 若未启用 */
          if (!domainFunctionAnnotation.enabled()) {
            continue;
          }

          String[] paths = null != domainFunctionAnnotation.value() && domainFunctionAnnotation.value().length > 0
              ? domainFunctionAnnotation.value()
              : new String[]{domainMethod.getName()};

          String url;
          for (String path : paths) {
            url = PathUtils.getPrettyUrl((StringUtils.hasText(domainModelApiPrefix) ? domainModelApiPrefix : commonApiPrefix) + "/" + domainModelName + "/" + path);
            try {
              RequestMappingInfo requestMappingInfo = RequestMappingInfo
                  .paths(url)
                  .methods(domainFunctionAnnotation.method())
                  .produces(MediaType.APPLICATION_JSON_VALUE)
                  .consumes(MediaType.APPLICATION_JSON_VALUE)
                  .build();

              if (!requestMappingHandlerMapping.getHandlerMethods().containsKey(requestMappingInfo)) {
                requestMappingHandlerMapping.registerMapping(
                    requestMappingInfo,
                    proxyDomainObject,
                    proxyDomainMethod
                );

                logger.debug("Scanned domain model: {}::{}", domainClass.getName(), url);
              }
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }
        }
      }
    }
  }

}
