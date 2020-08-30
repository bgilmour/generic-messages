package com.langtoun.messages.annotations;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Target;

@Target(METHOD)
public @interface FieldOrder {

  /**
   * A list of field names in the order that they are to be processed. Some
   * serialization components may regard this as a suggestion whereas other will
   * require it in order to produce a valid encoding.
   * 
   * @return a list of strings
   */
  String[] value() default {};

}
