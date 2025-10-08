package io.latte.boot.entity.cmd;

import java.util.List;

/**
 * EntitiesCommand
 *
 * @author : wugz
 * @since : 2025/7/10
 */
public class EntitiesCommand<T extends EntityCommand> implements ICommand {
  private List<?> insertEntities;
  private List<?> updateEntities;
  private List<?> deleteEntities;

  public List<T> getInsertEntities() {
    return (List<T>) insertEntities;
  }

  public void setInsertEntities(List<?> insertEntities) {
    this.insertEntities = insertEntities;
  }

  public List<T> getUpdateEntities() {
    return (List<T>) updateEntities;
  }

  public void setUpdateEntities(List<?> updateEntities) {
    this.updateEntities = updateEntities;
  }

  public List<T> getDeleteEntities() {
    return (List<T>) deleteEntities;
  }

  public void setDeleteEntities(List<?> deleteEntities) {
    this.deleteEntities = deleteEntities;
  }
}
