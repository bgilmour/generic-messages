package com.langtoun.messages.types;

public enum FieldEncodingType {

  UNKNOWN(""),
  BASE64("base64"),
  JSON("json"),
  JSON_URLENCODED("json+urlencoded"),
  XML("xml"),
  XML_URLENCODED("xml+urlencoded");

  private FieldEncodingType(String encodingType) {
    this.encodingType = encodingType;
  }

  public final String encodingType;

}
