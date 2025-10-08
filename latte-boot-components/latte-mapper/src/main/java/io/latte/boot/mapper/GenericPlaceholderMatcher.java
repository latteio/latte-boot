package io.latte.boot.mapper;

import io.latte.boot.support.web.IdUtils;

/**
 * GenericPlaceholderMatcher
 *
 * @author : wugz
 * @since : 2023/7/27
 */
public class GenericPlaceholderMatcher {
  private static final String TOKEN = "?";

  /**
   * 构造函数
   */
  private GenericPlaceholderMatcher() {
  }

  /**
   * 分析text并替换其中的token
   *
   * @param text
   * @return
   */
  public static String matchAndReplace(String text) {
    if (!text.contains(TOKEN)) {
      return text;
    }

    StringBuilder textBuilder = new StringBuilder();
    for (int e = 0; e < text.length(); ++e) {
      char chr = text.charAt(e);
      if (chr == TOKEN.charAt(0)) {
        textBuilder.append("#{PARAM__COLUMN__")
            .append(IdUtils.getId())
            .append("}");
      } else {
        textBuilder.append(chr);
      }
    }

    return textBuilder.toString();
  }
}
