package com.langtoun.messages.generic;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.langtoun.messages.types.SerializablePayload;

@SuppressWarnings("serial")
public class CustomTypeResolverBuilder extends DefaultTypeResolverBuilder {

  @SuppressWarnings("deprecation")
  public CustomTypeResolverBuilder() {
    super(DefaultTyping.OBJECT_AND_NON_CONCRETE);
  }

  @Override
  public boolean useForType(JavaType javaType) {
    final boolean result = SerializablePayload.class.isAssignableFrom(javaType.getRawClass());
    System.out.println("checking " + javaType + " => " + result);
    return result;
  }
}
