package io.latte.boot.webmvc.advicer;

import io.latte.boot.security.exception.UnauthorizedException;
import io.latte.boot.security.exception.ValidateCodeException;
import io.latte.boot.support.web.JsonUtils;
import io.latte.boot.web.exception.RequestLimitException;
import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.message.MessageUtils;
import io.latte.boot.web.http.response.ApiResponse;
import io.latte.boot.webmvc.ApplicationProperties;
import org.apache.ibatis.javassist.NotFoundException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.login.AccountExpiredException;
import java.util.Map;

/**
 * CommonExceptionAdvice
 *
 * @author : wugz
 * @since : 2021/4/21
 */
@ControllerAdvice
public class CommonExceptionAdvice {
  private final Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);
  private final ApplicationProperties applicationProperties;

  /**
   * 构造函数
   *
   * @param applicationProperties
   */
  public CommonExceptionAdvice(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  /**
   * 认证异常
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = AuthenticationException.class)
  @ResponseBody
  public ApiResponse<Object> exceptionHandle(AuthenticationException exception) {
    if (applicationProperties.getException().isUseTrace()) {
      exception.printStackTrace();
    }

    Throwable exceptionCause = exception instanceof InternalAuthenticationServiceException ? exception.getCause() : exception;
    if (exceptionCause instanceof UnauthorizedException) {
      logger.error(UnauthorizedException.class.getSimpleName() + ": {}: url = {}", exception.getMessage(), ((UnauthorizedException) exception).getUrl());
      return ApiResponse.failure(MessageCodec.UNAUTHORIZED.status(), MessageUtils.getMessage(MessageCodec.UNAUTHORIZED));
    } else {
      logger.error(AuthenticationException.class.getSimpleName() + ": {}", null != exception.getCause() ? exception.getCause().getMessage() : exception.getMessage());
      if (exceptionCause instanceof UsernameNotFoundException) {
        return ApiResponse.failure(MessageCodec.USERNAME_NOTFOUND.status(), MessageUtils.getMessage(MessageCodec.USERNAME_NOTFOUND));
      } else if (exceptionCause instanceof LockedException) {
        return ApiResponse.failure(MessageCodec.ACCOUNT_LOCKED.status(), MessageUtils.getMessage(MessageCodec.ACCOUNT_LOCKED));
      } else if (exceptionCause instanceof AccountExpiredException) {
        return ApiResponse.failure(MessageCodec.ACCOUNT_EXPIRED.status(), MessageUtils.getMessage(MessageCodec.ACCOUNT_EXPIRED));
      } else if (exceptionCause instanceof DisabledException) {
        return ApiResponse.failure(MessageCodec.ACCOUNT_DISABLED.status(), MessageUtils.getMessage(MessageCodec.ACCOUNT_DISABLED));
      } else if (exceptionCause instanceof BadCredentialsException) {
        return ApiResponse.failure(MessageCodec.BAD_CREDENTIALS.status(), MessageUtils.getMessage(MessageCodec.BAD_CREDENTIALS));
      } else if (exceptionCause instanceof CredentialsExpiredException) {
        return ApiResponse.failure(MessageCodec.CREDENTIALS_EXPIRED.status(), MessageUtils.getMessage(MessageCodec.CREDENTIALS_EXPIRED));
      } else if (exceptionCause instanceof ValidateCodeException) {
        final ValidateCodeException validateCodeException = (ValidateCodeException) exceptionCause;
        return ApiResponse.failure(validateCodeException.getStatus(), validateCodeException.getMessage());
      } else if (exceptionCause instanceof SessionAuthenticationException) {
        return ApiResponse.failure(MessageCodec.SESSION_AUTH_FAILURE.status(), MessageUtils.getMessage(MessageCodec.SESSION_AUTH_FAILURE));
      }
      return ApiResponse.failure(MessageCodec.INTERNAL_SERVER_ERROR.status(), MessageUtils.getMessage(MessageCodec.INTERNAL_SERVER_ERROR));
    }
  }

  /**
   * 403异常
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = AccessDeniedException.class)
  @ResponseBody
  public ApiResponse<Object> exceptionHandle(AccessDeniedException exception) {
    logger.error(AccessDeniedException.class.getSimpleName() + ": {}", exception.getMessage());
    if (applicationProperties.getException().isUseTrace()) {
      exception.printStackTrace();
    } else {
      logger.error(AccessDeniedException.class.getSimpleName() + ": {}", null != exception.getCause() ? exception.getCause().getMessage() : exception.getMessage());
    }

    return ApiResponse.failure(MessageCodec.FORBIDDEN);
  }

  /**
   * 404异常
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = NotFoundException.class)
  @ResponseBody
  public ApiResponse<Object> exceptionHandle(NotFoundException exception) {
    logger.error(NotFoundException.class.getSimpleName() + ": {}", exception.getMessage());
    if (applicationProperties.getException().isUseTrace()) {
      exception.printStackTrace();
    } else {
      logger.error(NotFoundException.class.getSimpleName() + ": {}", null != exception.getCause() ? exception.getCause().getMessage() : exception.getMessage());
    }

    return ApiResponse.failure(MessageCodec.NOT_FOUND);
  }

  /**
   * 访问频率限制异常(按429处理)
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = RequestLimitException.class)
  @ResponseBody
  public ApiResponse<Object> exceptionHandle(RequestLimitException exception) {
    logger.error(RequestLimitException.class.getSimpleName() + ": {}: url = {}", exception.getMessage(), exception.getUrl());
    if (applicationProperties.getException().isUseTrace()) {
      exception.printStackTrace();
    } else {
      logger.error(RequestLimitException.class.getSimpleName() + ": {}", null != exception.getCause() ? exception.getCause().getMessage() : exception.getMessage());
    }

    return ApiResponse.failure(MessageCodec.REQUEST_BUSY);
  }

  /**
   * 500异常: 参数校验错误
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = BindException.class)
  @ResponseBody
  public ApiResponse<Object> exceptionHandle(BindException exception) {
    logger.error(BindException.class.getSimpleName() + ": {}", exception.getMessage());
    if (applicationProperties.getException().isUseTrace()) {
      exception.printStackTrace();
    } else {
      logger.error(BindException.class.getSimpleName() + ": {}", null != exception.getCause() ? exception.getCause().getMessage() : exception.getMessage());
    }

    JSONObject errorMsgs = new JSONObject();
    BindingResult bindingResult = exception.getBindingResult();
    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      errorMsgs.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

    return ApiResponse.failure(MessageCodec.VALIDATION_FAILURE);
  }

  /**
   * 500异常
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = BadCredentialsException.class)
  @ResponseBody
  public ApiResponse<Object> exceptionHandle(BadCredentialsException exception) {
    logger.error(AuthenticationException.class.getSimpleName() + ": {}", exception.getMessage());
    if (applicationProperties.getException().isUseTrace()) {
      exception.printStackTrace();
    } else {
      logger.error(BadCredentialsException.class.getSimpleName() + ": {}", null != exception.getCause() ? exception.getCause().getMessage() : exception.getMessage());
    }

    return ApiResponse.failure(MessageCodec.INTERNAL_SERVER_ERROR);
  }

  /**
   * 运行时500异常
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = RuntimeException.class)
  @ResponseBody
  public ApiResponse<Object> exceptionHandle(RuntimeException exception) {
    logger.error(RuntimeException.class.getSimpleName() + ": {}", exception.getMessage());
    if (applicationProperties.getException().isUseTrace()) {
      exception.printStackTrace();
    } else {
      logger.error(RuntimeException.class.getSimpleName() + ": {}", null != exception.getCause() ? exception.getCause().getMessage() : exception.getMessage());
    }

    ApiResponse<Object> model = null;
    try {
      /* 如果e.getMessage()是JSON字符串, 则解析成标准Model数据; 否则按普通异常字符串处理 */
      String message = exception.getMessage();
      Map<String, Object> exceptionJson = message.startsWith("{") && message.endsWith("}") ? JsonUtils.toMap(message) : null;
      if (null != exceptionJson && exceptionJson.containsKey("status") && exceptionJson.containsKey("msg") && exceptionJson.containsKey("data")) {
        model = ApiResponse.failure(Integer.parseInt(exceptionJson.get("status").toString()), exceptionJson.get("msg").toString(), exceptionJson.get("data"));
      } else {
        throw new RuntimeException(exception);
      }
    } catch (Exception ex) {
      model = ApiResponse.failure(MessageCodec.INTERNAL_SERVER_ERROR);
    }

    return model;
  }

  /**
   * 默认异常按500处理
   *
   * @param exception
   * @return
   */
  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public ApiResponse<Object> exceptionHandle(Exception exception) {
    logger.error(Exception.class.getSimpleName() + ": {}", exception.getMessage());
    if (applicationProperties.getException().isUseTrace()) {
      exception.printStackTrace();
    } else {
      logger.error(Exception.class.getSimpleName() + ": {}", null != exception.getCause() ? exception.getCause().getMessage() : exception.getMessage());
    }

    return ApiResponse.failure(MessageCodec.INTERNAL_SERVER_ERROR);
  }

}
