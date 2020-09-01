package com.langtoun.messages.types;

public enum CustomTypeCodec {

  STD(""),
  GQL("gql");

  private CustomTypeCodec(String customCodec) {
    this.customCodec = customCodec;
  }

  public final String customCodec;

}
