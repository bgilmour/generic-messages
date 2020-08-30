package com.langtoun.messages.types;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;

/**
 * Interface for payload types that are to be handled by generic serializers and
 * deserializers.
 */
@JsonSerialize(using = PayloadJsonSerializer.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class)
public interface SerializablePayload {

  /**
   * Is the top level type a list.
   * 
   * @return {@code true} if the type defines a list, defaults to {@code false}
   */
  default boolean isListType() { return false; }

}
