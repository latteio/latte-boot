package io.latte.boot.web.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * EnumTypeValidator
 *
 * @author : wugz
 * @since : 2021/12/13
 */
public class EnumTypeValidator<T> implements ConstraintValidator<EnumType, T>, ApplicationContextAware {
  private static Logger logger = LoggerFactory.getLogger(EnumTypeValidator.class);

  private ApplicationContext applicationContext;
  private EnumType constraintAnnotation;

  /**
   * 按code码返回枚举实例
   * (内部使用)
   *
   * @param enumTypeClass
   * @param code
   * @return
   */
  @SuppressWarnings(value = "unchecked")
  static <E extends Enum<E>, T> IEnumType<T> internalCodeOf(Class<E> enumTypeClass, T code) {
    final Class<E> targetClass = Objects.requireNonNull(
        enumTypeClass,
        "Argument 'enumTypeClass' cannot be null");

    IEnumType<T>[] enumEntries = (IEnumType<T>[]) targetClass.getEnumConstants();
    if (null != enumEntries) {
      for (IEnumType<T> enumEntry : enumEntries) {
        if (enumEntry.getCode().equals(code)) {
          return enumEntry;
        }
      }
    }
    return null;
  }

  /**
   * initialize
   *
   * @param constraintAnnotation 注解
   */
  public void initialize(EnumType constraintAnnotation) {
    this.constraintAnnotation = constraintAnnotation;
  }

  /**
   * 校验方法
   *
   * @param value   校验值
   * @param context 校验上下文
   * @return 校验结果
   */
  @SuppressWarnings("unchecked")
  public boolean isValid(T value, ConstraintValidatorContext context) {
    /* 1.如果value不包含值 */
    if (ObjectUtils.isEmpty(value)) {
      return this.constraintAnnotation.nullable();
    }

    /* 2.在keys()返回集合中校验 */
    try {
      final Class<? extends Enum> typeProviderClass = this.constraintAnnotation.typeProvider();
      if (IEnumType.class.isAssignableFrom(typeProviderClass)) {
        return null != internalCodeOf(typeProviderClass, value);
      } else {
        return null != Enum.valueOf(typeProviderClass, String.valueOf(value));
      }
    } catch (Exception ex) {
      logger.error("Unknown enum type: {}", value);
      return false;
    }
  }

  /**
   * Set the ApplicationContext
   *
   * @param applicationContext 应用上下文
   * @throws BeansException 异常
   */
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

}
