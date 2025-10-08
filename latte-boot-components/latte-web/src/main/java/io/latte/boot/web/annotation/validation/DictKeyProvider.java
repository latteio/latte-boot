package io.latte.boot.web.annotation.validation;

import java.util.List;

/**
 * DictKeyProvider
 *
 * @author : wugz
 * @since : 2021/12/13
 */
public interface DictKeyProvider {
  /**
   * 返回键名集
   *
   * @param category 目录名
   * @return 键名集
   */
  List<String> getDictKeys(String category);
}
