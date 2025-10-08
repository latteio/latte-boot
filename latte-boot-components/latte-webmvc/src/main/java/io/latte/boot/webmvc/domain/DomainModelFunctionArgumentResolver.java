package io.latte.boot.webmvc.domain;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.latte.boot.entity.cmd.EntitiesCommand;
import io.latte.boot.entity.cmd.EntityCommand;
import io.latte.boot.entity.cmd.ICommand;
import io.latte.boot.entity.query.IQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 通用领域模型方法参数解析器
 *
 * @author : wugz
 * @since : 2025/7/11
 */
public class DomainModelFunctionArgumentResolver implements HandlerMethodArgumentResolver {
  private final ObjectMapper objectMapper;
  private final Validator validator;

  public DomainModelFunctionArgumentResolver(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  public boolean supportsParameter(MethodParameter parameter) {
    return ICommand.class.isAssignableFrom(parameter.getParameterType())
        || IQuery.class.isAssignableFrom(parameter.getParameterType());
  }

  public Object resolveArgument(MethodParameter parameter,
                                @Nullable ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest,
                                @Nullable WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

    Object parameterValue = EntitiesCommand.class.isAssignableFrom(parameter.getParameterType())
        ? getParameterValueForEntitiesCommandType(parameter, webRequest)
        : objectMapper.readValue(request.getInputStream(), parameter.getParameterType());

    /* 参数校验 */
    validateParameterValue(parameter, parameterValue);

    return parameterValue;
  }

  /**
   * 参数校验
   *
   * @param parameter      参数对象
   * @param parameterValue 参数值
   */
  private void validateParameterValue(MethodParameter parameter, Object parameterValue) {
    if (null != parameter.getParameter().getAnnotation(Validated.class)) {
      Set<ConstraintViolation<Object>> validate = validator.validate(parameterValue);
      if (!validate.isEmpty()) {
        throw new RuntimeException(validate.stream()
            .map(c -> c.getPropertyPath().toString().concat(c.getMessage()))
            .collect(Collectors.joining(", ")));
      }
    }
  }

  private Class<? extends EntityCommand> getCommandType(MethodParameter parameter) {
    ResolvableType resolvableType = ResolvableType.forMethodParameter(parameter);
    ResolvableType genericType = resolvableType.getGeneric(0);
    Class<?> genericClass = genericType.resolve();

    if (genericClass != null && EntityCommand.class.isAssignableFrom(genericClass)) {
      return (Class<? extends EntityCommand>) genericClass;
    }

    throw new IllegalArgumentException("Unable to determine the generic parameter type for 'EntitiesCommand'");
  }

  private Object getParameterValueForEntitiesCommandType(MethodParameter parameter, NativeWebRequest webRequest) throws IOException {
    /* 获取泛型类型参数 */
    Class<? extends EntityCommand> commandType = getCommandType(parameter);

    /* 获取请求体 */
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    String body = request.getReader().lines().collect(Collectors.joining());

    /* 构造JavaType */
    JavaType type = objectMapper.getTypeFactory().constructParametricType(EntitiesCommand.class, commandType);
    JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, commandType);

    /* 解析JSON */
    JsonNode rootNode = objectMapper.readTree(body);
    EntitiesCommand<?> entitiesCommand = objectMapper.readValue(body, type);

    /* 解析JSON: insertEntities, updateEntities, deleteEntities */
    if (rootNode.has("insertEntities")) {
      List<?> insertList = objectMapper.readValue(rootNode.get("insertEntities").toString(), listType);
      entitiesCommand.setInsertEntities(insertList);
    }

    if (rootNode.has("updateEntities")) {
      List<?> updateList = objectMapper.readValue(rootNode.get("updateEntities").toString(), listType);
      entitiesCommand.setUpdateEntities(updateList);
    }

    if (rootNode.has("deleteEntities")) {
      List<?> deleteList = objectMapper.readValue(rootNode.get("deleteEntities").toString(), listType);
      entitiesCommand.setDeleteEntities(deleteList);
    }

    return entitiesCommand;
  }

}
