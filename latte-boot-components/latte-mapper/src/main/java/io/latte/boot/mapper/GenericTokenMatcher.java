package io.latte.boot.mapper;

/**
 * GenericTokenMatcher
 *
 * @author : wugz
 * @since : 2021/7/10
 */
public class GenericTokenMatcher {

  private final String openToken;
  private final String closeToken;

  /**
   * 构造函数
   *
   * @param openToken  token的前缀
   * @param closeToken token的后缀
   */
  public GenericTokenMatcher(String openToken, String closeToken) {
    this.openToken = openToken;
    this.closeToken = closeToken;
  }

  /**
   * 分析text并匹配其中的token
   *
   * @param text
   * @return
   */
  public MatchedToken parse(String text) {
    if (text == null || text.isEmpty()) {
      return null;
    }

    // search open token
    int start = text.indexOf(openToken);
    if (start == -1) {
      return null;
    }

    MatchedToken matched = new MatchedToken(text);
    char[] src = text.toCharArray();
    int offset = 0;
    StringBuilder expression = null;
    while (start > -1) {
      if (start > 0 && src[start - 1] == '\\') {
        // this open token is escaped. remove the backslash and continue.
        offset = start + openToken.length();
      } else {
        // found open token. let's search close token.
        if (expression == null) {
          expression = new StringBuilder();
        } else {
          expression.setLength(0);
        }
        offset = start + openToken.length();
        int end = text.indexOf(closeToken, offset);
        while (end > -1) {
          if (end > offset && src[end - 1] == '\\') {
            // this close token is escaped. remove the backslash and continue.
            expression.append(src, offset, end - offset - 1).append(closeToken);
            offset = end + closeToken.length();
            end = text.indexOf(closeToken, offset);
          } else {
            expression.append(src, offset, end - offset);
            break;
          }
        }
        if (end == -1) {
          // close token was not found.
          offset = src.length;
        } else {
          matched.addToken(expression.toString());
          offset = end + closeToken.length();
        }
      }
      start = text.indexOf(openToken, offset);
    }

    return matched;
  }

}
