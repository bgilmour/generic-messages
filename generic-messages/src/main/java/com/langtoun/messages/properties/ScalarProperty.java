package com.langtoun.messages.properties;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ScalarProperty extends MessageProperty {

  private final Supplier<Object> getter;
  private final Consumer<Object> setter;

  protected ScalarProperty(final String name, final String jsonName, final String xmlName, final boolean required, final Supplier<Object> getter,
      final Consumer<Object> setter, final Class<?> valueType) {
    super(name, jsonName, xmlName, required, valueType);
    this.getter = getter;
    this.setter = setter;
  }

  public static ScalarProperty newScalarProperty(final String name, final String jsonName, final String xmlName, final boolean required,
      final Supplier<Object> getter, final Consumer<Object> setter, final Class<?> valueType) {
    return new ScalarProperty(name, jsonName, xmlName, required, getter, setter, valueType);
  }

  public Supplier<Object> getGetter() {
    return getter;
  }

  public Consumer<Object> getSetter() {
    return setter;
  }

  @Override
  public String toString() {
    return "scalarProp[super=" + super.toString() + "]]";
  }

}
