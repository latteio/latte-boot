package io.latte.boot.webmvc.service;

import io.latte.boot.entity.cmd.EntitiesCommand;
import io.latte.boot.entity.cmd.EntityCommand;
import io.latte.boot.entity.cmd.IdCommand;
import io.latte.boot.entity.query.Page;
import io.latte.boot.entity.query.PageQuery;
import io.latte.boot.web.http.message.MessageUtils;
import io.latte.boot.web.http.response.ApiResponse;
import io.latte.boot.webmvc.core.IService;
import io.latte.boot.webmvc.domain.IDomainModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * BaseService
 *
 * @author : wugz
 * @since : 2022/2/22
 */
public class BaseService<D extends IDomainModel<C, Q, T>, C extends EntityCommand, Q extends PageQuery, T>
    implements IService {
  protected final D domainModel;

  /**
   * 构造函数
   *
   * @param domainModel 领域模型
   */
  public BaseService(D domainModel) {
    this.domainModel = domainModel;
  }

  /**
   * 返回分页数据
   *
   * @param query 查询参数
   * @return
   */
  public ApiResponse<Page<T>> getPageData(Q query) {
    return ApiResponse.success(domainModel.getPageData(query));
  }

  /**
   * 返回列表数据
   *
   * @param query 查询参数
   * @return
   */
  public ApiResponse<List<T>> getListData(Q query) {
    return ApiResponse.success(domainModel.getListData(query));
  }

  /**
   * 返回一条数据
   *
   * @param query 查询参数
   * @return
   */
  public ApiResponse<T> getData(Q query) {
    return ApiResponse.success(domainModel.getData(query));
  }

  /**
   * 保存数据
   *
   * @param cmd 数据参数
   * @return
   */
  @Transactional
  public ApiResponse<Boolean> save(C cmd) {
    return domainModel.save(cmd)
        ? ApiResponse.success()
        : throwFailure(MessageUtils.getDefaultFailureMsg());
  }

  /**
   * 保存数据
   *
   * @param cmd 数据参数
   * @return
   */
  @Transactional
  public ApiResponse<T> saveWithReturns(C cmd) {
    T data = domainModel.saveWithReturns(cmd);
    return null != data
        ? ApiResponse.success(data)
        : throwFailure(MessageUtils.getDefaultFailureMsg());
  }

  /**
   * 更新数据
   *
   * @param cmd 数据参数
   * @return
   */
  @Transactional
  public ApiResponse<Boolean> updateById(C cmd) {
    return domainModel.updateById(cmd)
        ? ApiResponse.success()
        : throwFailure(MessageUtils.getDefaultFailureMsg());
  }

  /**
   * 保存或更新数据
   *
   * @param cmd 数据参数
   * @return
   */
  @Transactional
  public ApiResponse<Boolean> saveOrUpdate(C cmd) {
    Boolean result = StringUtils.hasText(cmd.getId())
        ? domainModel.updateById(cmd)
        : domainModel.save(cmd);

    return result
        ? ApiResponse.success(true)
        : throwFailure(MessageUtils.getDefaultFailureMsg());
  }

  /**
   * 删除数据
   *
   * @param cmd 数据参数
   * @return
   */
  @Transactional
  public ApiResponse<Boolean> delete(IdCommand cmd) {
    return domainModel.delete(cmd)
        ? ApiResponse.success(true)
        : throwFailure(MessageUtils.getDefaultFailureMsg());
  }

  /**
   * 批量操作
   *
   * @param entities 数据参数
   * @return
   */
  @Transactional
  public ApiResponse<Integer> batchSave(EntitiesCommand<C> entities) {
    Integer total = domainModel.batchSave(entities);
    return total > 0
        ? ApiResponse.success(total)
        : throwFailure(MessageUtils.getDefaultFailureMsg());
  }
}
