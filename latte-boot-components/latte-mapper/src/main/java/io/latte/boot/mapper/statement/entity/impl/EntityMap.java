package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.mapper.GenericTokenMatcher;
import io.latte.boot.mapper.MatchedToken;
import io.latte.boot.mapper.statement.entity.IEntityMap;
import io.latte.boot.support.web.IdUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EntityMap
 *
 * @author : wugz
 * @since : 2024/10/18
 */
public class EntityMap extends HashMap<String, Object> implements IEntityMap {
  /**
   * 返回已绑定参数
   *
   * @return values
   */
  public Map<String, Object> getParameters() {
    return this;
  }

  /**
   * 动态绑定表达式
   *
   * @param expressions 表达式
   * @param values      表达式占位符的值
   */
  public void bindingParameters(String expressions, Object... values) {
    GenericTokenMatcher matcher = new GenericTokenMatcher("#{", "}");
    MatchedToken matchedToken = matcher.parse(expressions);
    if (null != matchedToken) {
      final List<String> tokens = matchedToken.getTokens();
      for (int e = 0; e < tokens.size(); e++) {
        try {
          this.put(tokens.get(e), values[e]);
        } catch (Exception ex) {
          this.put(tokens.get(e), null);
        }
      }
    }
  }

  /**
   * 检查参数名称: 如果已经存在, 则生成一个新的并返回
   *
   * @param parameterName 参数名称
   */
  public String checkParameterName(String parameterName) {
    return this.containsKey(parameterName)
        ? (parameterName + IdUtils.getId())
        : parameterName;
  }
}
