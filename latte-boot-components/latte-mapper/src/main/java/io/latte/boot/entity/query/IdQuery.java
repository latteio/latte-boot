package io.latte.boot.entity.query;

import jakarta.validation.constraints.NotBlank;

/**
 * IdQuery
 *
 * @author : wugz
 * @since : 2022/1/18
 */
public class IdQuery implements IQuery {
  @NotBlank
  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.getClass().getSimpleName());
    builder.append("(");
    builder.append("id=").append(id);
    builder.append(")");
    return builder.toString();
  }
}