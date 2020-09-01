package com.langtoun.messages.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.langtoun.messages.types.FieldEncodingType;

/**
 * An annotation that can be added to fields that are to participate in
 * serialization / deserialization. Fields that are not annotated or that don't
 * define a name for the serialization being processed are ignored.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface TypeProperty {

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
   * The name of the field as it should appear in a JSON serialization.
   * 
   * @return the JSON property name
   */
  String jsonName() default "";

  /**
   * The name of the field as it should appear in an XML serialization.
   * 
   * @return the XML element name
   */
  String xmlName() default "";

  /**
   * The name of the field as it appeared in the API specification. It may not
   * match the field name if it used characters or conventions that are not
   * supported for Java identifiers.
   * 
   * @return the original field name from the API specification
   */
  String originalName() default "";

  /**
   * If the field is a member of a type that is subject to custom encoding then
   * this is the encoding that is to be used / parsed when the field is serialized
   * / deserialized.
   * 
   * @return the type of encoding for the field
   */
  FieldEncodingType encoding() default FieldEncodingType.UNKNOWN;
}
