package io.latte.boot.webmvc.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.latte.boot.entity.cmd.EntitiesCommand;
import io.latte.boot.entity.cmd.EntityCommand;
import io.latte.boot.entity.cmd.IdCommand;
import io.latte.boot.entity.query.PageQuery;
import io.latte.boot.support.validate.Validate;
import io.latte.boot.web.annotation.component.DomainModel;
import io.latte.boot.webmvc.ApplicationProperties;
import io.latte.boot.webmvc.domain.IDomainModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * GenericAbstractController
 *
 * @author : wugz
 * @since : 2024/11/2
 */
class GenericAbstractController {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private final ApplicationProperties applicationProperties;
  private final ObjectMapper objectMapper;
  private final Validator validator;
  protected ApplicationContext applicationContext;

  /**
   * 构造函数
   *
   * @param applicationProperties
   * @param objectMapper
   */
  public GenericAbstractController(ApplicationProperties applicationProperties, ObjectMapper objectMapper) {
    this.applicationProperties = applicationProperties;
    this.objectMapper = objectMapper;
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  /**
   * 校验领域对象
   *
   * @param domainModelName 领域模型名
   * @param methodName      方法名
   * @param params          方法实参
   * @return 领域模型代理对象
   */
  protected ProxyDomainModelWrapper validateDomainModel(String domainModelName, String methodName, Object params) {
    /* Step 1: DomainModel 实例对象判空 */
    IDomainModel<?, ?, ?> proxyDomainObject = Validate.requireNonNull(applicationContext.getBean(domainModelName, IDomainModel.class),
        "Component named '%s' was not found or may not be subclass of '%s'",
        domainModelName,
        IDomainModel.class.getSimpleName());

    /* Step 2: DomainModel 实例对象(可能是代理对象, 若是则返回代理对象的目标对象)被 @DomainModel 注解 */
    Class<?> domainClass = AopUtils.getTargetClass(proxyDomainObject);
    Validate.requireNonNull(domainClass.getAnnotation(DomainModel.class),
        "Component named '%s' must declare with annotation '@DomainModel'",
        domainClass.getSimpleName());

    /* Step 3: 校验并获取方法 */
    Method proxyDomainMethod = validateDomainModelMethod(proxyDomainObject, domainClass, methodName, params);

    /* Step 4: 组织返回参数 */
    ProxyDomainModelWrapper proxyDomainModelWrapper = new ProxyDomainModelWrapper(
        proxyDomainObject,
        proxyDomainObject.getClass(),
        proxyDomainMethod,
        params,
        domainClass
    );

    /* 打印跟踪信息 */
    checkIfTrace(proxyDomainModelWrapper);

    return proxyDomainModelWrapper;
  }

  /**
   * 校验领域模型的方法
   *
   * @param proxyDomainObject 领域模型实例代理对象
   * @param serviceClass      源领域模型类
   * @param methodName        方法名
   * @param params            方法实参
   */
  private Method validateDomainModelMethod(IDomainModel<?, ?, ?> proxyDomainObject, Class<?> serviceClass, String methodName, Object params) {
    /* 验证方法名称: 同名且必须是public方法 */
    List<Method> publicMethods = Arrays.stream(ReflectionUtils.getUniqueDeclaredMethods(proxyDomainObject.getClass()))
        .filter(m -> m.getName().equals(methodName) && Modifier.isPublic(m.getModifiers()))
        .toList();

    Method targetMethod = null;
    if (publicMethods.isEmpty()) {
      throw new RuntimeException(String.format("Method '%s' not found or not public in component '%s'",
          methodName,
          serviceClass.getSimpleName()));
    } else {
      /* 查询到多个同名方法: 属于方法重载的情况, 根据参数长度和类型进行匹配 */
      /* 查询到多个同名方法: 先匹配一个参数的情况 */
      if (!List.class.isAssignableFrom(params.getClass())) {
        /* 匹配一个参数的情况: 仍然考虑有重载的情况 */
        List<Method> methods = publicMethods.stream()
            .filter(m -> m.getParameters().length == 1)
            .toList();

        for (Method method : methods) {
          if (validateDomainModelMethodParameters(method, params, 1)) {
            targetMethod = method;
            break;
          }
        }

        /* 查询到多个同名方法: 匹配无参数的情况(PS: 同名且无参的方法若存在, 则有且仅有一个, 否则不满足方法重载的要求) */
        if (null == targetMethod) {
          methods = publicMethods.stream()
              .filter(m -> m.getParameters().length == 0)
              .toList();
          if (!methods.isEmpty()) {
            targetMethod = methods.get(0);
          }
        }
      } else {
        /* 查询到多个同名方法: 匹配多参数的情况 */
        int expectedParameterLength = ((List<?>) params).size();
        List<Method> methods = publicMethods.stream()
            .filter(m -> m.getParameters().length == expectedParameterLength)
            .toList();

        for (Method method : methods) {
          if (validateDomainModelMethodParameters(method, params, expectedParameterLength)) {
            targetMethod = method;
            break;
          }
        }
      }
    }

    Validate.requireNonNull(targetMethod,
        String.format("Method '%s' in component '%s' validate failed",
            methodName,
            serviceClass.getSimpleName()));

    return targetMethod;
  }

  /**
   * 参数校验
   *
   * @param parameter      目标方法的参数
   * @param parameterValue 目标方法的参数值
   */
  private void validateDomainModelMethodParameter(Parameter parameter, Object parameterValue) throws RuntimeException {
    if (null != parameter.getAnnotation(Validated.class)) {
      Set<ConstraintViolation<Object>> validate = validator.validate(parameterValue);
      if (!validate.isEmpty()) {
        throw new RuntimeException(validate.stream()
            .map(c -> c.getPropertyPath().toString().concat(c.getMessage()))
            .collect(Collectors.joining(", ")));
      }
    }
  }

  /**
   * 参数匹配: 通过 @RequestBody 传参, 类型只会是List类型, Map类型以及一般对象类型
   *
   * @param proxyMethodObject       代理方法
   * @param parameterValue          参数值
   * @param expectedParameterLength 预期参数长度
   * @return
   */
  private boolean validateDomainModelMethodParameters(Method proxyMethodObject, Object parameterValue, int expectedParameterLength) {
    boolean valid = false;
    Class<?>[] parameterTypes = proxyMethodObject.getParameterTypes();
    try {
      /* 1.包含1个参数 */
      if (expectedParameterLength == 1) {
        Class<?> parameterType = parameterTypes[0];
        if (Map.class.isAssignableFrom(parameterValue.getClass())) {
//          Map<String, Field> allFields = ClassUtils.getAllFields(parameterType, true);
//          Optional<?> objectAny = ((Map<?, ?>) parameterValue).keySet()
//              .stream()
//              .filter(key -> allFields.containsKey(key))
//              .findAny();
//          valid = objectAny.isPresent();
          valid = true;
        } else {
          valid = parameterType.isAssignableFrom(parameterValue.getClass());
        }
      } else {
        /* 2.包含多个参数 */
        List<?> parameterValues = (List<?>) parameterValue;
        int count = 0;
        Class<?> parameterType;
        for (int i = 0; i < parameterValues.size(); i++) {
          parameterType = parameterTypes[i];
          if (null == parameterValues.get(i)) {
            count++;
          } else if (Map.class.isAssignableFrom(parameterValues.get(i).getClass())) {
//            Map<String, Field> allFields = ClassUtils.getAllFields(parameterType, true);
//            Optional<?> objectAny = ((Map<?, ?>) parameterValues.get(i)).keySet()
//                .stream()
//                .filter(key -> allFields.containsKey(key))
//                .findAny();
//            if (objectAny.isPresent()) count++;
            count++;
          } else if (parameterType.isAssignableFrom(parameterValues.get(i).getClass())) {
            count++;
          }
        }

        valid = (count == expectedParameterLength);
      }
    } catch (Exception ignored) {
      valid = false;
    }

    return valid;
  }

  /**
   * 转换成cmd参数并校验
   *
   * @param proxyDomainModelWrapper 领域模型代理对象
   * @param <C>
   * @return
   */
  protected <C extends EntityCommand> C convertAndValidateCommandParams(ProxyDomainModelWrapper proxyDomainModelWrapper) {
    ResolvableType type = ResolvableType.forClass(proxyDomainModelWrapper.getDomainClass()).getSuperType();
    Class<C> cmdClass = (Class<C>) type.getGeneric(1).resolve();
    C cmdObject = objectMapper.convertValue(proxyDomainModelWrapper.getParams(), cmdClass);

    /* 检测是否进行参数校验 */
    validateDomainModelMethodParameter(proxyDomainModelWrapper.getDomainMethod().getParameters()[0], cmdObject);

    return cmdObject;
  }

  /**
   * 转换成cmd数组参数并校验
   *
   * @param proxyDomainModelWrapper 领域模型代理对象
   * @param <C>
   * @return
   */
  protected <C extends EntityCommand> EntitiesCommand<C> convertAndValidateEntityCommandParams(ProxyDomainModelWrapper proxyDomainModelWrapper) {
    ResolvableType type = ResolvableType.forClass(proxyDomainModelWrapper.getDomainClass()).getSuperType();
    Class<C> cmdClass = (Class<C>) type.getGeneric(1).resolve();

    EntitiesCommand<C> returnParams = null;
    if (null != proxyDomainModelWrapper.getParams()) {
      JavaType javaType = objectMapper.getTypeFactory().constructParametricType(EntitiesCommand.class, cmdClass);
      returnParams = objectMapper.convertValue(proxyDomainModelWrapper.getParams(), javaType);
    }

    return returnParams;
  }

  /**
   * 转换成cmd参数并校验
   *
   * @param serviceMethod 方法
   * @param cmd           入参
   * @return
   */
  protected IdCommand convertAndValidateCommandParams(Method serviceMethod, IdCommand cmd) {
    /* 检测是否进行参数校验 */
    validateDomainModelMethodParameter(serviceMethod.getParameters()[0], cmd);

    return cmd;
  }

  /**
   * 转换成查询参数
   *
   * @param proxyDomainModelWrapper 领域模型代理对象
   * @param <Q>
   * @return
   */
  protected <Q extends PageQuery> Q convertQueryParams(ProxyDomainModelWrapper proxyDomainModelWrapper) {
    ResolvableType type = ResolvableType.forClass(proxyDomainModelWrapper.getDomainClass()).getSuperType();
    Class<Q> pageQueryClass = (Class<Q>) type.getGeneric(2).resolve();
    return objectMapper.convertValue(proxyDomainModelWrapper.getParams(), pageQueryClass);
  }

  /**
   * 参数转换
   *
   * @param proxyDomainModelWrapper 领域模型代理对象
   * @return
   */
  protected Object[] convertAndValidateParams(ProxyDomainModelWrapper proxyDomainModelWrapper) {
    Object[] parameters;
    Class<?>[] targetMethodParameterTypes = proxyDomainModelWrapper.getProxyDomainMethod().getParameterTypes();
    Parameter[] sourceMethodParameters = proxyDomainModelWrapper.getDomainMethod().getParameters();

    if (targetMethodParameterTypes.length == 0) {
      parameters = null;
    } else if (targetMethodParameterTypes.length == 1) {
      parameters = new Object[1];
      parameters[0] = objectMapper.convertValue(proxyDomainModelWrapper.getParams(), targetMethodParameterTypes[0]);
      /* 检测是否进行参数校验 */
      validateDomainModelMethodParameter(sourceMethodParameters[0], parameters[0]);
    } else {
      parameters = new Object[targetMethodParameterTypes.length];
      for (int i = 0; i < targetMethodParameterTypes.length; i++) {
        parameters[i] = objectMapper.convertValue(((List<?>) proxyDomainModelWrapper.getParams()).get(i), targetMethodParameterTypes[i]);
        validateDomainModelMethodParameter(sourceMethodParameters[i], parameters[i]);
      }
    }

    return parameters;
  }

  /**
   * 打印参数跟踪
   *
   * @param proxyDomainModelWrapper 领域模型代理对象
   */
  private void checkIfTrace(ProxyDomainModelWrapper proxyDomainModelWrapper) {
    if (applicationProperties.getSecurity().getHttp().getRequest().isUseTrace()) {
      logger.debug("\n ===> [Request method]: {}::{}\n ===> [Request params]: {}",
          proxyDomainModelWrapper.getDomainClass().getName(),
          proxyDomainModelWrapper.getDomainMethod().getName(),
          proxyDomainModelWrapper.getParams());
    }
  }

}
