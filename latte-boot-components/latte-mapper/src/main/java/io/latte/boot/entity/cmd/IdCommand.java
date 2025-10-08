package io.latte.boot.entity.cmd;

import jakarta.validation.constraints.NotBlank;

/**
 * IdCommand
 *
 * @author : wugz
 * @since : 2022/1/18
 */
public class IdCommand implements ICommand {
  @NotBlank
  private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}