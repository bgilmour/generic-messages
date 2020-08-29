package com.langtoun.messages.types.properties;

import static java.util.Objects.requireNonNull;

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

  protected ListProperty(final ListProperty property) {
    this(property.getName(), property.getJsonName(), property.getXmlName(), property.isRequired(), property.getValueType());
    this.getter = requireNonNull(property.getter, "list property getter cannot be null");
    this.setter = requireNonNull(property.setter, "list property setter cannot be null");
    this.itemType = requireNonNull(property.itemType, "list property itemType cannot be null");
    super.setTypeEncoding(property.getTypeEncoding());
  }

  public Supplier<List<Object>> getGetter() { return getter; }

  public Consumer<List<Object>> getSetter() { return setter; }

  public Class<?> getItemType() { return itemType; }

  @Override
  public String toString() {
    return "listProp[super=" + super.toString() + "], itemType=" + itemType.getTypeName() + "]";
  }

  public static class Builder {

    private ListProperty property; // partially complete object

    private Builder(final String name, final String jsonName, final String xmlName, final boolean required) {
      property = new ListProperty(name, jsonName, xmlName, required, List.class);
    }

    public static Builder newBuilder(final String name, final String jsonName, final String xmlName, final boolean required) {
      return new Builder(name, jsonName, xmlName, required);
    }

    public Builder getter(Supplier<List<Object>> getter) {
      property.getter = getter;
      return this;
    }

    public Builder setter(Consumer<List<Object>> setter) {
      property.setter = setter;
      return this;
    }

    public Builder itemType(Class<?> itemType) {
      property.itemType = itemType;
      return this;
    }

    public Builder typeEncoding(final String typeEncoding) {
      property.setTypeEncoding(typeEncoding);
      return this;
    }

    public ListProperty build() {
      return new ListProperty(property);
    }

  }

}
