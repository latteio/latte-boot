package io.latte.boot.entity.query;

/**
 * PageQuery
 *
 * @author : wugz
 * @since : 2020/5/23
 */
public class PageQuery implements IQuery {
  private int pageNum = Page.defaultPageNum();
  private int pageSize = Page.defaultPageSize();
  private String id;
  private String keywords;

  /**
   * 构造函数
   */
  public PageQuery() {
  }

  public int getPageNum() {
    return pageNum;
  }

  public void setPageNum(int pageNum) {
    this.pageNum = pageNum;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }
}
