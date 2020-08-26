package com.langtoun.messages.types;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.MessageJsonDeserializer;
import com.langtoun.messages.generic.MessageJsonSerializer;
import com.langtoun.messages.types.properties.MessageProperty;

/**
 * Interface for payload types that are to be transported by generic messages.
 */
@JsonSerialize(using = MessageJsonSerializer.class)
@JsonDeserialize(using = MessageJsonDeserializer.class)
public interface SerializablePayload {

  /**
   * Provide the configuration for the properties that are to be transported in
   * the generic message.
   *
   * @return a list of {@link MessageProperty} objects
   */
  List<MessageProperty> getProperties();

}
