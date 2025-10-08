package io.latte.boot.entity.query;

import java.io.Serializable;
import java.util.List;

/**
 * Page
 *
 * @author : wugz
 * @since : 2018/4/29
 */
public class Page<E> implements Serializable {
  private static final int PARAM_DEFAULT_PAGE_NUM = 1;
  private static final int PARAM_DEFAULT_PAGE_SIZE = 15;
  // 分页数据
  private List<E> records = null;
  // 总记录数
  private long total = 0;
  // 总页数
  private int pages;
  // 当前页
  private int pageNum;
  // 每页的数量
  private int pageSize;

  /**
   * 构造函数
   *
   * @param pageNum
   * @param pageSize
   * @param pages
   */
  public Page(int pageNum,
              int pageSize,
              int pages) {
    this.pageNum = pageNum;
    this.pageSize = pageSize;
    this.pages = pages;
  }

  /**
   * 默认起始页
   *
   * @return
   */
  public static int defaultPageNum() {
    return PARAM_DEFAULT_PAGE_NUM;
  }

  /**
   * 默认每页显示条数
   *
   * @return
   */
  public static int defaultPageSize() {
    return PARAM_DEFAULT_PAGE_SIZE;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public int getPages() {
    return pages;
  }

  public int getPageNum() {
    return pageNum;
  }

  public int getPageSize() {
    return pageSize;
  }

  public List<E> getRecords() {
    return records;
  }

  public void setRecords(List<E> records) {
    this.records = records;
  }
}
