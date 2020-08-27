package com.langtoun.messages.types.properties;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ScalarProperty extends PayloadProperty {

  private Supplier<Object> getter;
  private Consumer<Object> setter;

  protected ScalarProperty(final String name, final String jsonName, final String xmlName, final boolean required,
      final Class<?> valueType) {
    super(name, jsonName, xmlName, required, valueType);
  }

  public Supplier<Object> getGetter() { return getter; }

  public Consumer<Object> getSetter() { return setter; }

  @Override
  public String toString() {
    return "scalarProp[super=" + super.toString() + "]]";
  }

  public static class Builder {

    private static ScalarProperty property;

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

    public ScalarProperty build() {
      assert property.getter != null;
      assert property.setter != null;

      return property;
    }

  }

}
