package com.langtoun.messages.types;

import java.util.List;

import com.langtoun.messages.properties.MessageProperty;

public interface SerializablePayload {

  List<MessageProperty> getProperties();

}
