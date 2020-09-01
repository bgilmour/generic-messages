package com.langtoun.messages.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface AwsTypeDefinition {

  /**
   * This is an annotation that allows the developer / source generator to define
   * the order in which field names are processed by the serialization components.
   * While this won't be important for some encodings, it will required for others
   * so this implementation will also require it. The array may contain names of
   * fields that are in the annotated class or in any of its superclasses.
   * 
   * @return an array of field names
   */
  FieldOrder fieldOrder() default @FieldOrder;

  /**
   * Does the annotated type definition represents a list type.
   * 
   * @return {@code true} if the type is a list, otherwise {@code false}
   */
  boolean isList() default false;

  /**
   * An object that defines custom encoding parameters for the type definition.
   * 
   * @return a {@link CustomTypeEncoding} object
   */
  CustomTypeEncoding encoding() default @CustomTypeEncoding;

}
