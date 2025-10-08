package io.latte.boot.entity.query;

/**
 * TreeQuery
 *
 * @author : wugz
 * @since : 2023/7/16
 */
public class TreeQuery extends PageQuery {
  private boolean useRoot = false;

  public boolean isUseRoot() {
    return useRoot;
  }

  public void setUseRoot(boolean useRoot) {
    this.useRoot = useRoot;
  }
}
