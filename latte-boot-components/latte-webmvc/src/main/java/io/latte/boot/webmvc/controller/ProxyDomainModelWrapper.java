package io.latte.boot.webmvc.controller;

import io.latte.boot.webmvc.domain.IDomainModel;

import java.lang.reflect.Method;

/**
 * ProxyDomainModelWrapper
 *
 * @author : wugz
 * @since : 2024/11/12
 */
public final class ProxyDomainModelWrapper {
  /* 代理领域及方法 */
  private final IDomainModel<?, ?, ?> proxyDomainObject;
  private final Class<?> proxyDomainClass;
  private final Method proxyDomainMethod;
  private final Object params;

  /* 源领域类及方法 */
  private final Class<?> domainClass;
  private final Method domainMethod;

  /**
   * 构造函数
   *
   * @param proxyDomainObject 代理领域对象
   * @param proxyDomainClass  代理领域对象类
   * @param proxyDomainMethod 代理领域方法
   * @param params            代理领域方法入参
   * @param domainClass       源领域类
   */
  public ProxyDomainModelWrapper(IDomainModel<?, ?, ?> proxyDomainObject,
                                 Class<?> proxyDomainClass,
                                 Method proxyDomainMethod,
                                 Object params,
                                 Class<?> domainClass) {
    this.proxyDomainObject = proxyDomainObject;
    this.proxyDomainClass = proxyDomainClass;
    this.proxyDomainMethod = proxyDomainMethod;
    this.params = params;
    this.domainClass = domainClass;
    this.domainMethod = domainMethod(domainClass, proxyDomainMethod);
  }

  /**
   * 获取用户类方法
   *
   * @param domainModelClass  源领域模型对象
   * @param proxyMethodObject 目标方法对象
   * @return 源方法
   */
  private static Method domainMethod(Class<?> domainModelClass, Method proxyMethodObject) {
    try {
      return domainModelClass.getMethod(proxyMethodObject.getName(), proxyMethodObject.getParameterTypes());
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public IDomainModel<?, ?, ?> getProxyDomainObject() {
    return proxyDomainObject;
  }

  public Class<?> getProxyDomainClass() {
    return proxyDomainClass;
  }

  public Method getProxyDomainMethod() {
    return proxyDomainMethod;
  }

  public Object getParams() {
    return params;
  }

  public Class<?> getDomainClass() {
    return domainClass;
  }

  public Method getDomainMethod() {
    return domainMethod;
  }
}
