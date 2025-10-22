package io.latte.boot.entity.data;

import com.google.common.base.Strings;
import io.latte.boot.entity.TreeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StructuredTree
 * (自限定类型树)
 *
 * @author : wugz
 * @since : 2022/1/18
 */
public abstract class StructuredTree<T extends StructuredTree<T>> implements TreeEntity {
  private List<T> children = new ArrayList<>();

  /**
   * 构造函数
   */
  public StructuredTree() {
  }

  /**
   * getter
   *
   * @return
   */
  public List<T> getChildren() {
    return this.children;
  }

  /**
   * setter
   *
   * @param children
   */
  public void setChildren(List<T> children) {
    this.children = children;
  }

  /**
   * 结构化子代节点集
   * (遍历并设置直接后继子节点集)
   *
   * @param treeNodes
   */

  public void structuringChildren(List<T> treeNodes) {
    this.structuringChildren(treeNodes, false);
  }

  /**
   * 结构化子代节点集
   * (遍历并设置直接后继子节点集)
   *
   * @param treeNodes   树节点集
   * @param includeThis treeNodes是否包含当前节点, 如果是则需要排除。
   */
  public void structuringChildren(List<T> treeNodes, boolean includeThis) {
    /* 1.节点集判空 */
    if (null == treeNodes || treeNodes.isEmpty()) {
      return;
    }

    /* 2.若包含当前节点则从节点集中移除当前节点 */
    if (includeThis) {
      treeNodes.removeIf(t -> t.getId().equals(this.getId()));
    }

    /* 3.设置子节点 */
    /* 3.1 遍历并设置子节点 */
    List<T> childrenNodes = treeNodes.stream()
        .filter(t -> !Strings.isNullOrEmpty(t.getParentId()) && t.getParentId().equals(this.getId()))
        .collect(Collectors.toList());
    this.setChildren(childrenNodes);

    /* 3.2 从节点集中移除子节点 */
    for (final T node : this.getChildren()) {
      treeNodes.removeIf(t -> t.getId().equals(node.getId()));
    }

    /* 3.3 继续遍历子节点的子节点 */
    for (final T node : this.getChildren()) {
      node.structuringChildren(treeNodes, false);
    }
  }

}
