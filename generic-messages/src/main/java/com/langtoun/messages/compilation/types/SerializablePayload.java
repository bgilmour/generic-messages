package com.langtoun.messages.compilation.types;

import java.util.List;

import com.langtoun.messages.compilation.properties.MessageProperty;

/**
 * Interface for payload types that are to be transported by generic messages.
 */
public interface SerializablePayload {

  /**
   * Provide the configuration for the properties that are to be transported in
   * the generic message.
   *
   * @return a list of {@link MessageProperty} objects
   */
  List<MessageProperty> getProperties();

}
