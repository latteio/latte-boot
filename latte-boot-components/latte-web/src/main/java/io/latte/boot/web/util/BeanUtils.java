package io.latte.boot.web.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * BeanUtils
 *
 * @author : wugz
 * @since : 2022/2/8
 */
public class BeanUtils implements ApplicationContextAware {
  private static ApplicationContext applicationContext;

  public BeanUtils() {
  }

  public static String getActiveProfile() {
    return applicationContext.getEnvironment().getActiveProfiles()[0];
  }

  public static <T> T getBean(Class<T> requiredType) {
    return applicationContext.getBean(requiredType);
  }

  public static <T> T getBean(Class<T> requiredType, Object... args) {
    return applicationContext.getBean(requiredType, args);
  }

  public static <T> T getBean(String name) throws BeansException {
    return (T) applicationContext.getBean(name);
  }

  public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
    return applicationContext.getBean(name, requiredType);
  }

  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    BeanUtils.applicationContext = applicationContext;
  }

}
