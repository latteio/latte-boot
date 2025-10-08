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

import java.util.Arrays;

/**
 * DictKeyValidator
 *
 * @author : wugz
 * @since : 2021/12/13
 */
public class DictKeyValidator implements ConstraintValidator<DictKey, Object>, ApplicationContextAware {
  private static Logger logger = LoggerFactory.getLogger(DictKeyValidator.class);

  private ApplicationContext applicationContext;
  private DictKey constraintAnnotation;

  /**
   * initialize
   *
   * @param constraintAnnotation 注解
   */
  public void initialize(DictKey constraintAnnotation) {
    this.constraintAnnotation = constraintAnnotation;
  }

  /**
   * 校验方法
   *
   * @param value   校验值
   * @param context 校验上下文
   * @return 校验结果
   */
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    /* 1.如果value不包含值 */
    if (ObjectUtils.isEmpty(value)) {
      return this.constraintAnnotation.nullable();
    }

    /* 2.在keys()返回集合中校验 */
    final String targetValue = value.toString();
    if (this.constraintAnnotation.keys().length > 0) {
      return Arrays.asList(this.constraintAnnotation.keys()).contains(targetValue);
    }

    /* 3.在keyProvider(category)返回集合中校验 */
    String category = this.constraintAnnotation.category();
    if (category.isEmpty()) {
      return false;
    }

    try {
      DictKeyProvider keyProvider = this.applicationContext.getBean(this.constraintAnnotation.keyProvider());
      return keyProvider.getDictKeys(category).contains(targetValue);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    return false;
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
