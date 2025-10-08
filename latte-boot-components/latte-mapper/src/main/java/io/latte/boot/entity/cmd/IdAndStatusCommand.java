package io.latte.boot.entity.cmd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * IdAndStatusCommand
 *
 * @author: wugz
 * @since: 2025/5/14
 */
public class IdAndStatusCommand implements ICommand {
  @NotBlank
  private String id;

  @NotNull
  private Integer status;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
