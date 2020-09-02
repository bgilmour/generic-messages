package com.langtoun.messages.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.langtoun.messages.types.CustomTypeCodec;

/**
 * An annotation that marks a type as being subject to custom serialization /
 * deserialization. Custom serialization will be invoked if any of prefix,
 * suffix, or keyValSep is set or if the encoder is set to a known custom
 * encoder.
 * <p/>
 * 
 * The following example assumes a type with three fields (a, b, c) of type
 * string that are subject to JSON encoding. The class definition looks like:
 * 
 * <pre>
 *   &#64;CustomTypeEncoding(prefix = "[[", suffix = "]]", fieldSep = "//", keyValSep = "=")
 *   public class SomeType {
 *   ...
 *   }
 * </pre>
 * 
 * An encoded type might look like this:
 * 
 * <pre>
 *   [[a=val1//b=val2//c=val3]]
 * </pre>
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface CustomTypeEncoding {

  /**
   * The string that will appear as a prefix to a custom type encoding.
   * 
   * @return the custom type encoding prefix, or an empty string if there is no
   *         prefix
   */
  String prefix() default "";

  /**
   * The string that will appear as a suffix to a custom type encoding.
   * 
   * @return the custom type encoding suffix, or an empty string if there is no
   *         suffix
   */
  String suffix() default "";

  /**
   * An array containing strings that are deemed to be valid field separators for
   * this encoding. If no field separators are specified then it's still possible
   * to serialize the type but deserialization isn't possible without deeper
   * knowledge of the encoding. If multiple field separators are specified then
   * the first in the list will be chosen when encoding a field for serialization.
   * 
   * @return the custom type encoding field separator, or an empty string if there
   *         is no field separator
   */
  String[] fieldSep() default {};

  /**
   * The string that will separate the key and value in a custom type encoding.
   * 
   * @return the custom type encoding prefix
   */
  String keyValSep() default "";

  /**
   * The custom codec that should be used for encoding and decoding the type.
   * 
   * @return the custom type code
   */
  CustomTypeCodec codec() default CustomTypeCodec.NONE;
}
