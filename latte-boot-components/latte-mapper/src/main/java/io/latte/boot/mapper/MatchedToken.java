package io.latte.boot.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * MatchedToken
 *
 * @author : wugz
 * @since : 2021/7/10
 */
public class MatchedToken {
  private final String original;
  private final List<String> tokens;

  /**
   * 构造函数
   *
   * @param original
   */
  public MatchedToken(String original) {
    this.original = original;
    this.tokens = new ArrayList<>();
  }

  /**
   * 返回原始子串
   *
   * @return
   */
  public String getOriginal() {
    return original;
  }

  /**
   * 返回匹配到的token集
   *
   * @return
   */
  public List<String> getTokens() {
    return tokens;
  }

  /**
   * 添加token
   *
   * @param token
   */
  public void addToken(String token) {
    this.tokens.add(token);
  }

}
