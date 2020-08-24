package com.langtoun.messages.generic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.types.SerializablePayload;

/**
 * Generic message container.
 *
 * @param <T> the generic type representing the payload
 */
@JsonSerialize(using = MessageJsonSerializer.class)
@JsonDeserialize(using = MessageJsonDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message<T extends SerializablePayload> {

  private T payload;

  private Message() {
    // create using factory method
  }

  public static <T extends SerializablePayload> Message<T> newMessage(final T payload) {
    final Message<T> message = new Message<>();
    message.payload = payload;
    return message;
  }

  public T getPayload() {
    return payload;
  }

}
