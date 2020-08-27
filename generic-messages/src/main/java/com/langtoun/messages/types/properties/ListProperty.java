package com.langtoun.messages.types.properties;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ListProperty extends PayloadProperty {

  private Supplier<List<Object>> getter;
  private Consumer<List<Object>> setter;
  private Class<?> itemType;

  protected ListProperty(final String name, final String jsonName, final String xmlName, final boolean required,
      final Class<?> valueType) {
    super(name, jsonName, xmlName, required, valueType);
  }

  public Supplier<List<Object>> getGetter() { return getter; }

  public Consumer<List<Object>> getSetter() { return setter; }

  public Class<?> getItemType() { return itemType; }

  @Override
  public String toString() {
    return "listProp[super=" + super.toString() + "], itemType=" + itemType.getTypeName() + "]";
  }

  public static class Builder {

    private static ListProperty property;

    private Builder(final String name, final String jsonName, final String xmlName, final boolean required) {
      property = new ListProperty(name, jsonName, xmlName, required, List.class);
    }

    public static Builder newBuilder(final String name, final String jsonName, final String xmlName, final boolean required) {
      return new Builder(name, jsonName, xmlName, required);
    }

    public Builder addGetter(Supplier<List<Object>> getter) {
      property.getter = getter;
      return this;
    }

    public Builder addSetter(Consumer<List<Object>> setter) {
      property.setter = setter;
      return this;
    }

    public Builder addItemType(Class<?> itemType) {
      property.itemType = itemType;
      return this;
    }

    public ListProperty build() {
      assert property.getter != null;
      assert property.setter != null;
      assert property.itemType != null;

      return property;
    }

  }

}
