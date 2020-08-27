package com.langtoun.messages.types;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;
import com.langtoun.messages.types.properties.PayloadProperty;

/**
 * Interface for payload types that are to be handled by generic serializers and
 * deserializers.
 */
@JsonSerialize(using = PayloadJsonSerializer.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class)
public interface SerializablePayload {

  /**
   * Provide the configuration for the properties that are to be transported in
   * the generic message.
   *
   * @return a list of {@link PayloadProperty} objects
   */
  List<PayloadProperty> getProperties();

}
