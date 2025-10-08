package io.latte.boot.webmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.latte.boot.entity.cmd.IdCommand;
import io.latte.boot.entity.query.Page;
import io.latte.boot.web.http.response.ApiResponse;
import io.latte.boot.webmvc.ApplicationProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 通用控制器
 *
 * @author : wugz
 * @since : 2024/10/15
 */
// @RestController
// @RequestMapping(value = "${application.security.http.request.api-prefix:/api}")
public class GenericRestController extends GenericAbstractController implements ApplicationContextAware {
  /**
   * 构造函数
   */
  public GenericRestController(ApplicationProperties applicationProperties,
                               ObjectMapper objectMapper) {
    super(applicationProperties, objectMapper);
  }

  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    super.setApplicationContext(applicationContext);
  }

  /**
   * 获取分页数据
   *
   * @param domainModelName 领域模型名
   * @param params          查询参数
   * @return 分页数据
   */
  @RequestMapping(value = {"/{domainModelName}/pageData", "/{domainModelName}/getPageData"}, method = RequestMethod.POST)
  public ApiResponse<Page<?>> getPageData(@PathVariable(name = "domainModelName") String domainModelName, @RequestBody Object params) {
    ProxyDomainModelWrapper proxyDomainModelWrapper = validateDomainModel(domainModelName, "getPageData", params);
    return ApiResponse.success(proxyDomainModelWrapper.getProxyDomainObject().getPageData(convertQueryParams(proxyDomainModelWrapper)));
  }

  /**
   * 获取列表数据
   *
   * @param domainModelName 领域模型名
   * @param params          查询参数
   * @return 列表数据
   */
  @RequestMapping(value = {"/{domainModelName}/listData", "/{domainModelName}/getListData"}, method = RequestMethod.POST)
  public ApiResponse<List<?>> getListData(@PathVariable(name = "domainModelName") String domainModelName, @RequestBody Object params) {
    ProxyDomainModelWrapper proxyDomainModelWrapper = validateDomainModel(domainModelName, "getListData", params);
    return ApiResponse.success(proxyDomainModelWrapper.getProxyDomainObject().getListData(convertQueryParams(proxyDomainModelWrapper)));
  }

  /**
   * 获取单条数据
   *
   * @param domainModelName 领域模型名
   * @param params          查询参数
   * @return 单条数据
   */
  @RequestMapping(value = {"/{domainModelName}/data", "/{domainModelName}/getData"}, method = RequestMethod.POST)
  public ApiResponse<?> getData(@PathVariable(name = "domainModelName") String domainModelName, @RequestBody Object params) {
    ProxyDomainModelWrapper proxyDomainModelWrapper = validateDomainModel(domainModelName, "getData", params);
    return ApiResponse.success(proxyDomainModelWrapper.getProxyDomainObject().getData(convertQueryParams(proxyDomainModelWrapper)));
  }

  /**
   * 数据保存
   *
   * @param domainModelName 领域模型名
   * @param params          操作参数
   * @return 操作结果: 状态
   */
  @RequestMapping(value = {"/{domainModelName}/save"}, method = RequestMethod.POST)
  public ApiResponse<?> saveData(@PathVariable(name = "domainModelName") String domainModelName, @RequestBody Object params) {
    ProxyDomainModelWrapper proxyDomainModelWrapper = validateDomainModel(domainModelName, "save", params);
    return ApiResponse.success(proxyDomainModelWrapper.getProxyDomainObject().save(convertAndValidateCommandParams(proxyDomainModelWrapper)));
  }

  /**
   * 数据保存
   *
   * @param domainModelName 领域模型名
   * @param params          操作参数
   * @return 操作结果: 记录反馈
   */
  @RequestMapping(value = {"/{domainModelName}/saveWithReturns"}, method = RequestMethod.POST)
  public ApiResponse<?> saveWithReturnsData(@PathVariable(name = "domainModelName") String domainModelName, @RequestBody Object params) {
    ProxyDomainModelWrapper proxyDomainModelWrapper = validateDomainModel(domainModelName, "saveWithReturns", params);
    return ApiResponse.success(proxyDomainModelWrapper.getProxyDomainObject().saveWithReturns(convertAndValidateCommandParams(proxyDomainModelWrapper)));
  }

  /**
   * 数据保存或更新
   *
   * @param domainModelName 领域模型名
   * @param params          操作参数
   * @return 操作结果: 状态
   */
  @RequestMapping(value = {"/{domainModelName}/saveOrUpdate"}, method = RequestMethod.POST)
  public ApiResponse<?> saveOrUpdateData(@PathVariable(name = "domainModelName") String domainModelName, @RequestBody Object params) {
    ProxyDomainModelWrapper proxyDomainModelWrapper = validateDomainModel(domainModelName, "saveOrUpdate", params);
    return ApiResponse.success(proxyDomainModelWrapper.getProxyDomainObject().saveOrUpdate(convertAndValidateCommandParams(proxyDomainModelWrapper)));
  }

  /**
   * 数据更新
   *
   * @param domainModelName 领域模型名
   * @param params          操作参数
   * @return 操作结果: 状态
   */
  @RequestMapping(value = {"/{domainModelName}/update"}, method = RequestMethod.POST)
  public ApiResponse<?> updateData(@PathVariable(name = "domainModelName") String domainModelName, @RequestBody Object params) {
    ProxyDomainModelWrapper proxyDomainModelWrapper = validateDomainModel(domainModelName, "updateById", params);
    return ApiResponse.success(proxyDomainModelWrapper.getProxyDomainObject().updateById(convertAndValidateCommandParams(proxyDomainModelWrapper)));
  }

  /**
   * 数据删除
   *
   * @param domainModelName 领域模型名
   * @param params          操作参数
   * @return 操作结果: 状态
   */
  @RequestMapping(value = {"/{domainModelName}/delete"}, method = RequestMethod.POST)
  public ApiResponse<?> deleteData(@PathVariable(name = "domainModelName") String domainModelName, @RequestBody IdCommand params) {
    ProxyDomainModelWrapper proxyDomainModelWrapper = validateDomainModel(domainModelName, "delete", params);
    return ApiResponse.success(proxyDomainModelWrapper.getProxyDomainObject().delete(convertAndValidateCommandParams(proxyDomainModelWrapper.getDomainMethod(), params)));
  }

  /**
   * 数据批量操作
   *
   * @param domainModelName 领域模型名
   * @param params          操作参数
   * @return 操作结果: 状态
   */
  @RequestMapping(value = {"/{domainModelName}/batchSave"}, method = RequestMethod.POST)
  public ApiResponse<?> batchSaveData(@PathVariable(name = "domainModelName") String domainModelName, @RequestBody Object params) {
    ProxyDomainModelWrapper proxyDomainModelWrapper = validateDomainModel(domainModelName, "batchSave", params);
    return ApiResponse.success(proxyDomainModelWrapper.getProxyDomainObject().batchSave(convertAndValidateEntityCommandParams(proxyDomainModelWrapper)));
  }

  /**
   * 数据操作
   *
   * @param domainModelName 领域模型名
   * @param methodName      方法名
   * @param params          操作参数(要求不能为空, 可传空JSON)
   * @return 操作结果: 状态or数据
   */
  @RequestMapping(value = {"/{domainModelName}/{methodName}"}, method = RequestMethod.POST)
  public ApiResponse<?> handleData(@PathVariable(name = "domainModelName") String domainModelName,
                                   @PathVariable(name = "methodName") String methodName,
                                   @RequestBody Object params) {
    try {
      /* Step 1: 检查组件及方法 */
      ProxyDomainModelWrapper proxyDomainModelWrapper = validateDomainModel(domainModelName, methodName, params);

      /* Step 2: 执行方法 */
      Object[] parameters = convertAndValidateParams(proxyDomainModelWrapper);
      if (null != parameters) {
        return ApiResponse.success(proxyDomainModelWrapper.getProxyDomainMethod().invoke(proxyDomainModelWrapper.getProxyDomainObject(), parameters));
      } else {
        return ApiResponse.success(proxyDomainModelWrapper.getProxyDomainMethod().invoke(proxyDomainModelWrapper.getProxyDomainObject()));
      }
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException(null != e.getCause() ? e.getCause().getMessage() : e.getMessage());
    }
  }
}
