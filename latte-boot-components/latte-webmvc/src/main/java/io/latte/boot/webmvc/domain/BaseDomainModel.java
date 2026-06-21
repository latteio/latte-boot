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
import org.springframework.beans.BeanUtils;
import org.springframework.core.ResolvableType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * BaseDomainModel
 *
 * @author : wugz
 * @since  : 2022/2/22
 */
public class BaseDomainModel<R extends IRepository<C, Q, T>, C extends EntityCommand, Q extends PageQuery, T>
    implements IDomainModel<C, Q, T>, IService, ThrowableFailure {
  protected final R repository;
  private final Class<C> commandObjectClass;
  private final Class<Q> queryObjectClass;
  private final Class<T> dataObjectClass;

  /**
   * 构造函数
   *
   * @param repository repository
   */
  public BaseDomainModel(R repository) {
    this.repository = repository;
    ResolvableType type = ResolvableType.forClass(this.getClass()).getSuperType();
    this.commandObjectClass = (Class<C>) type.getGeneric(1).resolve();
    this.queryObjectClass = (Class<Q>) type.getGeneric(2).resolve();
    this.dataObjectClass = (Class<T>) type.getGeneric(3).resolve();
  }

  /**
   * 返回分页数据
   *
   * @param query 查询参数
   * @return
   */
  @DomainFunction(value = {"/pageData", "/getPageData"})
  public Page<T> getPageData(@RequestBody Q query) {
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
  public List<T> getListData(@RequestBody Q query) {
    return repository.selectList(query);
  }

  /**
   * 返回一条数据
   *
   * @param query 查询参数
   * @return
   */
  @DomainFunction(value = {"/getData"})
  public T getData(@RequestBody Q query) {
    List<T> listData = getListData(query);
    if (listData.size() > 1) {
      throwFailure("Query return multiple records, but expected one");
    } else if (!listData.isEmpty()) {
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
    Q query = instantiateQueryObject();
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
  public Boolean save(@RequestBody @Validated C cmd) {
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
  public T saveWithReturns(@RequestBody @Validated C cmd) {
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
  public Boolean updateById(@RequestBody @Validated C cmd) {
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
  public Boolean saveOrUpdate(@RequestBody @Validated C cmd) {
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
  public Boolean delete(@RequestBody @Validated IdCommand cmd) {
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
  public Integer batchSave(@RequestBody @Validated EntitiesCommand<C> entities) {
    return repository.batchSave(getUserContext(), entities);
  }

  /**
   * 返回实体命令类型对象
   *
   * @return
   */
  protected C instantiateCommandObject() {
    return BeanUtils.instantiateClass(commandObjectClass);
  }

  /**
   * 返回实体查询类型对象
   *
   * @return
   */
  protected Q instantiateQueryObject() {
    return BeanUtils.instantiateClass(queryObjectClass);
  }

  /**
   * 返回实体DO类型对象
   *
   * @return
   */
  protected T instantiateDataObject() {
    return BeanUtils.instantiateClass(dataObjectClass);
  }
}
