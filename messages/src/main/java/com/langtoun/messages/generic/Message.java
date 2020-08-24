package com.langtoun.messages.generic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.types.SerializablePayload;

@JsonSerialize(using = MessageSerializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message<T extends SerializablePayload> {

  private T payload;

  private Message() {
    // create using factory method
  }

  public T getPayload() {
    return payload;
  }

  public static <T extends SerializablePayload> Message<T> newMessage(T payload) {
    final Message<T> message = new Message<T>();
    message.payload = payload;
    return message;
  }

}
