package com.langtoun.messages.types;

public enum CustomTypeCodec {

  NONE(""),
  GQL("gql");

  private CustomTypeCodec(String customCodec) {
    this.customCodec = customCodec;
  }

  public final String customCodec;

}
