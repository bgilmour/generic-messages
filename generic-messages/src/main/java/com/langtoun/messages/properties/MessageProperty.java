package com.langtoun.messages.properties;

public class MessageProperty {

  private final String name;
  private final String jsonName;
  private final String xmlName;

  private final boolean required;

  private final Class<?> valueType;

  protected MessageProperty(final String name, final String jsonName, final String xmlName, final boolean required, final Class<?> valueType) {
    super();
    this.name = name;
    this.jsonName = jsonName;
    this.xmlName = xmlName;
    this.required = required;
    this.valueType = valueType;
  }

  public String getName() {
    return name;
  }

  public String getJsonName() {
    return jsonName;
  }

  public String getXmlName() {
    return xmlName;
  }

  public boolean isRequired() {
    return required;
  }

  public Class<?> getValueType() {
    return valueType;
  }

  @Override
  public String toString() {
    return "[name=" + name + ", required=" + required + ", value=" + valueType.getTypeName() + "]";
  }

}
