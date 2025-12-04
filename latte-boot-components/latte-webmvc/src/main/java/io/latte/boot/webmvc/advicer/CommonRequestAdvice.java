package io.latte.boot.webmvc.advicer;

import io.latte.boot.web.annotation.component.DomainModel;
import io.latte.boot.webmvc.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 统一请求Controller
 *
 * @author : wugz
 * @since : 2021/12/23
 */
@ControllerAdvice(annotations = {Controller.class, RestController.class, DomainModel.class})
public class CommonRequestAdvice implements RequestBodyAdvice {
  private static Logger logger = LoggerFactory.getLogger(CommonRequestAdvice.class);

  private ApplicationProperties applicationProperties;

  /**
   * 构造函数
   *
   * @param applicationProperties
   */
  public CommonRequestAdvice(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  /**
   * Invoked first to determine if this interceptor applies.
   *
   * @param methodParameter the method parameter
   * @param targetType      the target type, not necessarily the same as the method
   *                        parameter type, e.g. for {@code HttpEntity<String>}.
   * @param converterType   the selected converter type
   * @return whether this interceptor should be invoked or not
   */
  public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    return applicationProperties.getSecurity().getHttpRequest().isUseTrace();
  }

  /**
   * Invoked second before the request body is read and converted.
   *
   * @param inputMessage  the request
   * @param parameter     the target method parameter
   * @param targetType    the target type, not necessarily the same as the method
   *                      parameter type, e.g. for {@code HttpEntity<String>}.
   * @param converterType the converter used to deserialize the body
   * @return the input request or a new instance (never {@code null})
   */
  public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
    return inputMessage;
  }

  /**
   * Invoked third (and last) after the request body is converted to an Object.
   *
   * @param body          set to the converter Object before the first advice is called
   * @param inputMessage  the request
   * @param parameter     the target method parameter
   * @param targetType    the target type, not necessarily the same as the method
   *                      parameter type, e.g. for {@code HttpEntity<String>}.
   * @param converterType the converter used to deserialize the body
   * @return the same body or a new instance
   */
  public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    logger.debug("\n ===> [Request method]: {}::{}\n ===> [Request params]: {}",
        Objects.requireNonNull(parameter.getMethod()).getDeclaringClass().getName(),
        parameter.getMethod().getName(), body);

    return body;
  }

  /**
   * Invoked second (and last) if the body is empty.
   *
   * @param body          usually set to {@code null} before the first advice is called
   * @param inputMessage  the request
   * @param parameter     the method parameter
   * @param targetType    the target type, not necessarily the same as the method
   *                      parameter type, e.g. for {@code HttpEntity<String>}.
   * @param converterType the selected converter type
   * @return the value to use, or {@code null} which may then raise an
   * {@code HttpMessageNotReadableException} if the argument is required
   */
  @Nullable
  public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    return body;
  }
}
