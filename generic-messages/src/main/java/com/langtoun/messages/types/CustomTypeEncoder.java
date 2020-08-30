package com.langtoun.messages.types;

public enum CustomTypeEncoder {

  STD(""),
  GQL("gql");

  private CustomTypeEncoder(String customEncoder) {
    this.customEncoder = customEncoder;
  }

  public final String customEncoder;

}
