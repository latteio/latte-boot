package io.latte.boot.webmvc.domain;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.latte.boot.entity.cmd.EntitiesCommand;
import io.latte.boot.entity.cmd.EntityCommand;
import io.latte.boot.entity.cmd.IdCommand;
import io.latte.boot.entity.query.Page;
import io.latte.boot.entity.query.PageQuery;
import io.latte.boot.web.annotation.component.DomainFunction;
import io.latte.boot.web.exception.ThrowableFailure;
import io.latte.boot.webmvc.core.IService;
import io.latte.boot.webmvc.repository.IRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * BaseDomainModel
 *
 * @author : wugz
 * @since : 2022/2/22
 */
public class BaseDomainModel<R extends IRepository<C, Q, T>, C extends EntityCommand, Q extends PageQuery, T>
    implements IDomainModel<C, Q, T>, IService, ThrowableFailure {
  protected final R repository;

  /**
   * 构造函数
   *
   * @param repository repository
   */
  public BaseDomainModel(R repository) {
    this.repository = repository;
  }

  /**
   * 返回分页数据
   *
   * @param query 查询参数
   * @return
   */
  @DomainFunction(value = {"/pageData", "/getPageData"})
  public Page<T> getPageData(Q query) {
    /* 执行分页 */
    try (com.github.pagehelper.Page<Object> pageObject = PageHelper.startPage(
        query.getPageNum(),
        query.getPageSize(),
        true,
        true,
        null)) {

      List<T> records = getListData(query);
      PageInfo<T> pageInfo = new PageInfo<>(records);

      /* 分页包装 */
      Page<T> page = new Page<>(
          pageInfo.getPageNum(),
          pageInfo.getPageSize(),
          pageInfo.getPages()
      );

      page.setTotal(pageInfo.getTotal());
      page.setRecords(pageInfo.getList());

      return page;
    }
  }

  /**
   * 返回列表数据
   *
   * @param query 查询参数
   * @return
   */
  @DomainFunction(value = {"/listData", "/getListData"})
  public List<T> getListData(Q query) {
    return repository.selectList(query);
  }

  /**
   * 返回一条数据
   *
   * @param query 查询参数
   * @return
   */
  @DomainFunction(value = {"/getData"})
  public T getData(Q query) {
    List<T> listData = getListData(query);
    if (listData.size() > 1) {
      throwFailure("Query return multiple records, but expected one");
    } else if (listData.size() > 0) {
      return listData.get(0);
    }

    return null;
  }

  /**
   * 返回一条数据
   *
   * @param id 查询参数
   * @return
   */
  protected T getData(String id) {
    Q query = repository.instantiateQueryObjectClass();
    query.setId(id);

    return getData(query);
  }

  /**
   * 保存数据
   *
   * @param cmd 数据参数
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @DomainFunction(value = {"/save"})
  public Boolean save(@Validated C cmd) {
    return repository.save(getUserContext(), cmd);
  }

  /**
   * 保存数据
   *
   * @param cmd 数据参数
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @DomainFunction(value = {"/saveWithReturns"})
  public T saveWithReturns(@Validated C cmd) {
    return repository.saveAndReturn(getUserContext(), cmd);
  }

  /**
   * 更新数据
   *
   * @param cmd 数据参数
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @DomainFunction(value = {"/updateById"})
  public Boolean updateById(@Validated C cmd) {
    return repository.updateById(getUserContext(), cmd);
  }

  /**
   * 保存或更新数据
   *
   * @param cmd 数据参数
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @DomainFunction(value = {"/saveOrUpdate"})
  public Boolean saveOrUpdate(@Validated C cmd) {
    return StringUtils.hasText(cmd.getId())
        ? repository.updateById(getUserContext(), cmd)
        : repository.save(getUserContext(), cmd);
  }

  /**
   * 删除数据
   *
   * @param cmd 数据参数
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @DomainFunction(value = {"/delete"})
  public Boolean delete(@Validated IdCommand cmd) {
    return repository.delete(getUserContext(), cmd);
  }

  /**
   * 批量操作
   *
   * @param entities 数据参数
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  @DomainFunction(value = {"/batchSave"})
  public Integer batchSave(@Validated EntitiesCommand<C> entities) {
    return repository.batchSave(getUserContext(), entities);
  }
}
