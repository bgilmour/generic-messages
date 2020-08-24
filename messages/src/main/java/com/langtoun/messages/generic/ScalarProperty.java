package com.langtoun.messages.generic;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ScalarProperty extends MessageProperty {

  private final Supplier<Object> getter;
  private final Consumer<Object> setter;

  protected ScalarProperty(String name, String jsonName, String xmlName, boolean required, Supplier<Object> getter,
      Consumer<Object> setter, Class<?> valueType) {
    super(name, jsonName, xmlName, required, valueType);
    this.getter = getter;
    this.setter = setter;
  }

  public static ScalarProperty newScalarProperty(String name, String jsonName, String xmlName, boolean required,
      Supplier<Object> getter, Consumer<Object> setter, Class<?> valueType) {
    return new ScalarProperty(name, jsonName, xmlName, required, getter, setter, valueType);
  }

  public Supplier<Object> getGetter() {
    return getter;
  }

  public Consumer<Object> getSetter() {
    return setter;
  }

}
