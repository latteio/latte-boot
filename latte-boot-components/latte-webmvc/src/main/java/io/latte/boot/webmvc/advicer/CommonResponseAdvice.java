package io.latte.boot.webmvc.advicer;

import io.latte.boot.web.annotation.component.DomainModel;
import io.latte.boot.web.http.response.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一反馈Controller
 *
 * @author : wugz
 * @since : 2021/12/23
 */
@ControllerAdvice(annotations = {Controller.class, RestController.class, DomainModel.class})
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {
  /**
   * Whether this component supports the given controller method return type
   * and the selected {@code HttpMessageConverter} type.
   *
   * @param returnType    the return type
   * @param converterType the selected converter type
   * @return {@code true} if {@link #beforeBodyWrite} should be invoked;
   * {@code false} otherwise
   */
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    final String parameterType = returnType.getParameterType().getName();
    return !"void".equals(parameterType)
        && !org.springframework.http.ResponseEntity.class.getName().equals(parameterType)
        && !ApiResponse.class.getName().equals(parameterType);
  }

  /**
   * Invoked after an {@code HttpMessageConverter} is selected and just before
   * its write method is invoked.
   *
   * @param body                  the body to be written
   * @param returnType            the return type of the controller method
   * @param selectedContentType   the content type selected through content negotiation
   * @param selectedConverterType the converter type selected to write to the response
   * @param request               the current request
   * @param response              the current response
   * @return the body that was passed in or a modified (possibly new) instance
   */
  @Nullable
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
    if (!selectedContentType.includes(MediaType.APPLICATION_JSON)) {
      return body;
    }

    return ApiResponse.success(body);
  }

}
