package com.langtoun.messages.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.node.ValueNode;
import com.langtoun.messages.annotations.CustomTypeEncoding;
import com.langtoun.messages.annotations.TypeDefinition;
import com.langtoun.messages.annotations.TypeProperty;
import com.langtoun.messages.types.CustomTypeCodec;

public final class SerializationUtil {

  private static final Map<Class<?>, JAXBContext> jaxbContexts = new HashMap<>();

  private SerializationUtil() {
    // static utility methods
  }

  /**
   * Get a {@link JAXBContext} from the cache. If there is no entry for the {Code
   * Class} specified then create it and add it to the cache.
   *
   * @param clazz the class that requires a {@link JAXBContext}
   * @return a {@link JAXBContext} object
   */
  public static JAXBContext getJaxbContextFor(final Class<?> clazz) {
    return jaxbContexts.computeIfAbsent(clazz, c -> {
      try {
        return JAXBContext.newInstance(c);
      } catch (final JAXBException e) {
        throw new IllegalStateException(e);
      }
    });
  }

  /**
   * Inspect the supplied {@code Object} and return the {@link TypeDefinition}
   * annotation if it is present.
   *
   * @param object the object that is to be inspected
   * @return an instance of the {@link TypeDefinition} annotation, or {@code null)
   */
  public static TypeDefinition getTypeDefinition(final Object object) {
    if (object != null) {
      return getTypeDefinition(object.getClass());
    }
    return null;
  }

  /**
   * Inspect the supplied {@code Object} and return the {@link TypeDefinition}
   * annotation if it is present.
   *
   * @param object the object that is to be inspected
   * @return an instance of the {@link TypeDefinition} annotation, or {@code null)
   */
  public static TypeDefinition getTypeDefinition(final Class<?> clazz) {
    return clazz.getAnnotation(TypeDefinition.class);
  }

  /**
   * Given a {@link CustomTypeEncoding} annotation, determine whether or not the
   * type has been configured to use a custom type encoding.
   * 
   * @param typeEncoding the type encoding annotation
   * @return {@code true} if the annotation indicates a custom type encoding,
   *         otherwise {@code false}
   */
  public static boolean usesCustomTypeEncoding(final CustomTypeEncoding typeEncoding) {
    return !typeEncoding.prefix().isEmpty() || !typeEncoding.suffix().isEmpty() || !typeEncoding.keyValSep().isEmpty()
        || typeEncoding.codec() != CustomTypeCodec.STD;
  }

  /**
   * Retrieve a map of {@link Field} objects and {@link TypeProperties} from the
   * supplied {@code Object}. The map represents all fields that have been
   * annotated for processing by the generic serialization components.
   *
   * @param object the object that is to be search for annotated fields
   * @return a map of {@link Field}s and {@link TypeProperty} annotations
   */
  public static Map<Field, TypeProperty> getFieldProperties(final Object object) {
    return getFieldProperties(object.getClass());
  }

  /**
   * Retrieve a map of {@link Field} objects and {@link TypeProperties} from the
   * supplied {@code Class}. The map represents all fields that have been
   * annotated for processing by the generic serialization components.
   *
   * @param clazz the class that is to be search for annotated fields
   * @return a map of {@link Field}s and {@link TypeProperty} annotations
   */
  public static Map<Field, TypeProperty> getFieldProperties(final Class<?> clazz) {
    return Stream.of(clazz.getDeclaredFields()).filter(f -> f.isAnnotationPresent(TypeProperty.class))
        .collect(Collectors.toMap(Function.identity(), f -> f.getAnnotation(TypeProperty.class)));
  }

  /**
   * Retrieve a list of superclasses of the supplied {@code Class}. The entries in
   * the list will be ordered from Object to the most immediate superclass.
   *
   * @param clazz the class whose superclasses are to be retrieved
   * @returna list of {@code Class} objects
   */
  public static List<Class<?>> getSuperclasses(final Class<?> clazz) {
    final List<Class<?>> superclasses = new ArrayList<>();
    getSuperclasses(clazz, superclasses);
    return superclasses;
  }

  /**
   * Retrieve a list of all classes in the class hierarchy of the supplied
   * {@code Class}. The entries in the list will be ordered from Object to the
   * class supplied as an argument.
   *
   * @param clazz the class whose class hierarchy is to be retrieved
   * @returna list of {@code Class} objects
   */
  public static List<Class<?>> getClassHierarchy(final Class<?> clazz) {
    final List<Class<?>> superclasses = new ArrayList<>();
    getSuperclasses(clazz, superclasses);
    superclasses.add(clazz);
    return superclasses;
  }

  private static void getSuperclasses(final Class<?> clazz, final List<Class<?>> superclasses) {
    final Class<?> superclass = clazz.getSuperclass();
    if (superclass != null) {
      getSuperclasses(superclass, superclasses);
      superclasses.add(superclass);
    }
  }

  /**
   * Retrieve a {@code Map} of field names and pairs of {@link Field} objects and
   * {@link TypeProperty} annotations for annotated fields in the supplied
   * {@code Class}.
   *
   * @param clazz the class whose annotated fields are to be retrieved
   * @return a map of pairs of {@link Field} objects and {@link TypeProperty}
   *         annotations
   */
  public static Map<String, Pair<Field, TypeProperty>> getDeclaredFieldsWithTypeProperty(final Class<?> clazz) {
    return getDeclaredFieldsWithAnnotation(Stream.of(clazz.getDeclaredFields()));
  }

  /**
   * Retrieve a {@code Map} of field names and pairs of {@link Field} objects and
   * {@link TypeProperty} annotations for annotated fields in all superclasses of
   * the supplied {@code Class}.
   *
   * @param clazz the class whose superclasses' annotated fields are to be
   *              retrieved
   * @return a map of pairs of {@link Field} objects and {@link TypeProperty}
   *         annotations
   */
  public static Map<String, Pair<Field, TypeProperty>> getSuperclassFieldsWithTypeProperty(final Class<?> clazz) {
    return getDeclaredFieldsWithAnnotation(getSuperclasses(clazz).stream().flatMap(c -> Stream.of(c.getDeclaredFields())));
  }

  /**
   * Retrieve a {@code Map} of field names and pairs of {@link Field} objects and
   * {@link TypeProperty} annotations for annotated fields in the class hierarchy
   * of the supplied {@code Class}.
   *
   * @param clazz the class whose class hierarchy's annotated fields are to be
   *              retrieved
   * @return a map of pairs of {@link Field} objects and {@link TypeProperty}
   *         annotations
   */
  public static Map<String, Pair<Field, TypeProperty>> getHierarchyFieldsWithTypeProperty(final Class<?> clazz) {
    return getDeclaredFieldsWithAnnotation(getClassHierarchy(clazz).stream().flatMap(c -> Stream.of(c.getDeclaredFields())));
  }

  private static Map<String, Pair<Field, TypeProperty>> getDeclaredFieldsWithAnnotation(final Stream<Field> stream) {
    return stream.filter(f -> f.isAnnotationPresent(TypeProperty.class))
        .collect(Collectors.toMap(f -> f.getName(), f -> Pair.of(f, f.getAnnotation(TypeProperty.class))));
  }

  /**
   * Compute the array of field names that defines the field order. If present,
   * prefer the array from the TypeDefinition annotation, otherwise return the key
   * set from the field properties map as an array.
   *
   * @param typeDefinition  the {@link TypeDefinition} from the annotated type
   * @param fieldProperties the field properties map calculated by traversing the
   *                        annotated type's class hierarchy
   * @return an array of field names that defines the field order
   */
  public static String[] computeFieldOrder(final TypeDefinition typeDefinition, final Map<String, ?> fieldProperties) {
    return typeDefinition.fieldOrder().value().length > 0 ? typeDefinition.fieldOrder().value()
        : fieldProperties.keySet().toArray(new String[] {});
  }

  /**
   * Create a regex pattern from the array of field separators that can be used to
   * tokenize a string.
   * 
   * @param separators the array of field separator strings
   * @return a regex pattern that quotes the separator strings as required
   */
  public static String createSeparatorPattern(final String[] separators) {
    return Stream.of(separators).map(Pattern::quote).collect(Collectors.joining("|"));
  }

  /**
   * When processing a field whose raw type is {@code List} the serialization
   * components need to know the actual type argument in order to create instances
   * of the correct type.
   * 
   * @param field the {@link Field} whose type is required
   * @return the {@code Class} of the actual type argument from
   *         {@link ParameterizedType#getActualTypeArguments} if the field type is
   *         parameterized, otherwise the {@code Class} from {@link Field#getType}
   */
  public static Class<?> getValueType(final Field field) {
    final Type type = field.getGenericType();
    if (type instanceof ParameterizedType) {
      final ParameterizedType paramType = (ParameterizedType) type;
      return (Class<?>) paramType.getActualTypeArguments()[0];
    } else {
      return field.getType();
    }
  }

  // TODO: add more Java types
  public static Object coerceFromNode(final ValueNode valueNode, final String fieldName) {
    if (valueNode.isNull()) {
      return null;
    } else if (valueNode.isIntegralNumber()) {
      return valueNode.asInt();
    } else if (valueNode.isFloatingPointNumber()) {
      return valueNode.asDouble();
    } else if (valueNode.isBoolean()) {
      return valueNode.asBoolean();
    } else {
      return valueNode.asText();
    }
  }

  public static Object coerceFromString(final String encodedValue, final Class<?> clazz) {
    if (String.class.isAssignableFrom(clazz)) {
      return encodedValue;
    } else if (Integer.class.isAssignableFrom(clazz)) {
      return Integer.parseInt(encodedValue);
    } else if (Double.class.isAssignableFrom(clazz)) {
      return Double.parseDouble(encodedValue);
    } else if (Boolean.class.isAssignableFrom(clazz)) {
      return Boolean.parseBoolean(encodedValue);
    } else {
      throw new IllegalArgumentException(String.format("attempt to deserialize unknown value type[%s]", clazz.getSimpleName()));
    }
  }

  public static Object getValue(final Object object, final Field field) {
    try {
      field.setAccessible(true);
      return field.get(object);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new IllegalArgumentException(
          String.format("failed to retrieve value from type[%s], field[%s]", object.getClass().getTypeName(), field.getName()), e);
    }
  }

  @SuppressWarnings("unchecked")
  public static List<Object> getListValue(final Object object, final Field field) {
    try {
      field.setAccessible(true);
      return (List<Object>) field.get(object);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new IllegalArgumentException(
          String.format("failed to retrieve list value from type[%s], field[%s]", object.getClass().getTypeName(), field.getName()),
          e);
    }
  }

  public static void setValue(final Object instance, final Object value, final Field field) {
    try {
      field.setAccessible(true);
      field.set(instance, value);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new IllegalArgumentException(
          String.format("failed to set value on type[%s], field[%s]", instance.getClass().getTypeName(), field.getName()), e);
    }
  }

}