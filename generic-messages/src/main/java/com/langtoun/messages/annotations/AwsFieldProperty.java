package com.langtoun.messages.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.langtoun.messages.types.FieldEncodingType;

/**
 * An annotation that can be added to fields that are to participate in
 * serialization / deserialization. The annotation allows the field to be
 * configured as required/optional and sensitive/non-sensitive. If the type that
 * contains the field is subject to a custom encoding then the field's encoding
 * can be specified using this annotation.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface AwsFieldProperty {

  /**
   * Determines whether the field is required or optional.
   * 
   * @return {@code true} if the field is required, otherwise {@code false}
   */
  boolean required() default false;

  /**
   * Determines whether the field is to be treated as sensitive data.
   * 
   * @return {@code true} if the field is sensitive, otherwise {@code false}
   */
  boolean isSensitive() default false;

  /**
   * If the field is a member of a type that is subject to custom encoding then
   * this is the encoding that is to be used / parsed when the field is serialized
   * / deserialized.
   * 
   * @return the type of encoding for the field
   */
  FieldEncodingType encoding() default FieldEncodingType.UNKNOWN;
}
