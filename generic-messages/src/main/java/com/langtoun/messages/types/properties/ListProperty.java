package com.langtoun.messages.types.properties;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ListProperty extends MessageProperty {

  private final Supplier<List<Object>> getter;
  private final Consumer<List<Object>> setter;

  private final Class<?> itemType;

  protected ListProperty(final String name, final String jsonName, final String xmlName, final boolean required, final Supplier<List<Object>> getter,
      final Consumer<List<Object>> setter, final Class<?> valueType, final Class<?> itemType) {
    super(name, jsonName, xmlName, required, valueType);
    this.getter = getter;
    this.setter = setter;
    this.itemType = itemType;
  }

  public static ListProperty newListProperty(final String name, final String jsonName, final String xmlName, final boolean required,
      final Supplier<List<Object>> getter, final Consumer<List<Object>> setter, final Class<?> valueType, final Class<?> itemType) {
    return new ListProperty(name, jsonName, xmlName, required, getter, setter, valueType, itemType);
  }

  public Supplier<List<Object>> getGetter() {
    return getter;
  }

  public Consumer<List<Object>> getSetter() {
    return setter;
  }

  public Class<?> getItemType() {
    return itemType;
  }

  @Override
  public String toString() {
    return "listProp[super=" + super.toString() + "], itemType=" + itemType.getTypeName() + "]";
  }

}
