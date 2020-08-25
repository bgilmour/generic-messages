package com.langtoun.messages.generic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  static {
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private T payload;

  protected Message() {
    // create using factory method
  }

  public T getPayload() {
    return payload;
  }

  public void setPayload(final T payload) {
    this.payload = payload;
  }

  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  public static <T extends SerializablePayload> Message<T> from(final T payload) {
    final Message<T> message = new Message<>();
    message.payload = payload;
    return message;
  }

  public static <T extends SerializablePayload> Message<T> from(final String json, final TypeReference<Message<T>> typeReference) {
    try {
      return OBJECT_MAPPER.readValue(json, typeReference);
    } catch (final JsonProcessingException e) {
      throw new IllegalArgumentException("unable to de-serialize an instance of " + typeReference.getType());
    }
  }

}
