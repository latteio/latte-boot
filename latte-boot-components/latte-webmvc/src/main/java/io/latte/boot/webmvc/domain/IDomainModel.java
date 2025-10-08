package io.latte.boot.webmvc.domain;

import io.latte.boot.entity.cmd.EntitiesCommand;
import io.latte.boot.entity.cmd.EntityCommand;
import io.latte.boot.entity.cmd.IdCommand;
import io.latte.boot.entity.query.Page;
import io.latte.boot.entity.query.PageQuery;

import java.util.List;

/**
 * IDomainModel
 *
 * @author : wugz
 * @since : 2022/2/22
 */
public interface IDomainModel<C extends EntityCommand, Q extends PageQuery, T> {
  /**
   * 返回分页数据
   *
   * @param query 查询参数
   * @return
   */
  Page<T> getPageData(Q query);

  /**
   * 返回列表数据
   *
   * @param query 查询参数
   * @return
   */
  List<T> getListData(Q query);

  /**
   * 返回一条数据
   *
   * @param query 查询参数
   * @return
   */
  T getData(Q query);

  /**
   * 保存数据
   *
   * @param cmd 数据参数
   * @return
   */
  Boolean save(C cmd);

  /**
   * 保存数据
   *
   * @param cmd 数据参数
   * @return
   */
  T saveWithReturns(C cmd);

  /**
   * 更新数据
   *
   * @param cmd 数据参数
   * @return
   */
  Boolean updateById(C cmd);

  /**
   * 保存或更新数据
   *
   * @param cmd 数据参数
   * @return
   */
  Boolean saveOrUpdate(C cmd);

  /**
   * 删除数据
   *
   * @param cmd 数据参数
   * @return
   */
  Boolean delete(IdCommand cmd);

  /**
   * 批量操作
   *
   * @param entities 数据参数
   * @return
   */
  Integer batchSave(EntitiesCommand<C> entities);
}
