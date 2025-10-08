package io.latte.boot.mapper;

/**
 * EntityPropertyOrMethodMeta
 *
 * @author : wugz
 * @since : 2021/6/30
 */
public class EntityPropertyOrMethodMeta {
  private final String className;
  private final String getterName;
  private final String propertyName;
  private final String fieldName;

  public EntityPropertyOrMethodMeta(String className,
                                    String getterName,
                                    String propertyName,
                                    String fieldName) {
    this.className = className;
    this.getterName = getterName;
    this.propertyName = propertyName;
    this.fieldName = fieldName;
  }

  public String getClassName() {
    return className;
  }

  public String getGetterName() {
    return getterName;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
