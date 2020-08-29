package com.langtoun.messages.types.properties;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ScalarProperty extends PayloadProperty {

  private Supplier<Object> getter;
  private Consumer<Object> setter;

  protected ScalarProperty(final String name, final String jsonName, final String xmlName, final boolean required,
      final Class<?> valueType) {
    super(name, jsonName, xmlName, required, valueType);
  }

  protected ScalarProperty(final ScalarProperty property) {
    this(property.getName(), property.getJsonName(), property.getXmlName(), property.isRequired(), property.getValueType());
    this.getter = requireNonNull(property.getter, "scalar property getter cannot be null");
    this.setter = requireNonNull(property.setter, "scalar property setter cannot be null");
    super.setTypeEncoding(property.getTypeEncoding());
  }

  public Supplier<Object> getGetter() { return getter; }

  public Consumer<Object> getSetter() { return setter; }

  @Override
  public String toString() {
    return "scalarProp[super=" + super.toString() + "]]";
  }

  public static class Builder {

    private ScalarProperty property; // partially complete object

    private Builder(final String name, final String jsonName, final String xmlName, final boolean required, Class<?> valueType) {
      property = new ScalarProperty(name, jsonName, xmlName, required, valueType);
    }

    public static Builder newBuilder(final String name, final String jsonName, final String xmlName, final boolean required,
        Class<?> valueType) {
      return new Builder(name, jsonName, xmlName, required, valueType);
    }

    public Builder addGetter(Supplier<Object> getter) {
      property.getter = getter;
      return this;
    }

    public Builder addSetter(Consumer<Object> setter) {
      property.setter = setter;
      return this;
    }

    public Builder typeEncoding(final String typeEncoding) {
      property.setTypeEncoding(typeEncoding);
      return this;
    }

    public ScalarProperty build() {
      return new ScalarProperty(property);
    }

  }

}
