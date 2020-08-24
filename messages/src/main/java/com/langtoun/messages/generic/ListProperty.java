package com.langtoun.messages.generic;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ListProperty extends MessageProperty {

  private final Supplier<List<Object>> getter;
  private final Consumer<List<Object>> setter;

  private final Class<?> itemType;

  protected ListProperty(String name, String jsonName, String xmlName, boolean required, Supplier<List<Object>> getter,
      Consumer<List<Object>> setter, Class<?> valueType, Class<?> itemType) {
    super(name, jsonName, xmlName, required, valueType);
    this.getter = getter;
    this.setter = setter;
    this.itemType = itemType;
  }

  public static ListProperty newListProperty(String name, String jsonName, String xmlName, boolean required,
      Supplier<List<Object>> getter, Consumer<List<Object>> setter, Class<?> valueType, Class<?> itemType) {
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

}
