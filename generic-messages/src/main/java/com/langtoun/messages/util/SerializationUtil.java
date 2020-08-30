package com.langtoun.messages.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.langtoun.messages.annotations.CustomTypeEncoding;
import com.langtoun.messages.annotations.TypeProperty;

public final class SerializationUtil {

  private SerializationUtil() {
    // static utility methods
  }

  /**
   * Inspect the supplied {@code Object} and return the {@link CustomTypeEncoding}
   * annotation if it is present.
   * 
   * @param object the object that is to be inspected
   * @return an instance of the {@link CustomTypeEncoding} annotation, or
   *         {@code null)
   */
  public static CustomTypeEncoding getCustomTypeEncoding(Object object) {
    if (object != null) {
      return getCustomTypeEncoding(object.getClass());
    }
    return null;
  }

  /**
   * Inspect the supplied {@code Class} and return the {@link CustomTypeEncoding}
   * annotation if it is present.
   * 
   * @param clazz the class that is to be inspected
   * @return an instance of the {@link CustomTypeEncoding} annotation, or
   *         {@code null)
   */
  public static CustomTypeEncoding getCustomTypeEncoding(Class<?> clazz) {
    return clazz.getAnnotation(CustomTypeEncoding.class);
  }

  /**
   * Retrieve a map of {@link Field} objects and {@link TypeProperties} from the
   * supplied {@code Object}. The map represents all fields that have been
   * annotated for processing by the generic serialization components.
   * 
   * @param object the object that is to be search for annotated fields
   * @return a map of {@link Field}s and {@link TypeProperty} annotations
   */
  public static Map<Field, TypeProperty> getFieldProperties(Object object) {
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
  public static Map<Field, TypeProperty> getFieldProperties(Class<?> clazz) {
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
  public static List<Class<?>> getSuperclasses(Class<?> clazz) {
    List<Class<?>> superclasses = new ArrayList<>();
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
  public static List<Class<?>> getClassHierarchy(Class<?> clazz) {
    List<Class<?>> superclasses = new ArrayList<>();
    getSuperclasses(clazz, superclasses);
    superclasses.add(clazz);
    return superclasses;
  }

  private static void getSuperclasses(Class<?> clazz, List<Class<?>> superclasses) {
    Class<?> superclass = clazz.getSuperclass();
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
  public static Map<String, Pair<Field, TypeProperty>> getDeclaredFieldsWithTypeProperty(Class<?> clazz) {
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
  public static Map<String, Pair<Field, TypeProperty>> getSuperclassFieldsWithTypeProperty(Class<?> clazz) {
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
  public static Map<String, Pair<Field, TypeProperty>> getHierarchyFieldsWithTypeProperty(Class<?> clazz) {
    return getDeclaredFieldsWithAnnotation(getClassHierarchy(clazz).stream().flatMap(c -> Stream.of(c.getDeclaredFields())));
  }

  private static Map<String, Pair<Field, TypeProperty>> getDeclaredFieldsWithAnnotation(Stream<Field> stream) {
    return stream.filter(f -> f.isAnnotationPresent(TypeProperty.class))
        .collect(Collectors.toMap(f -> f.getName(), f -> Pair.of(f, f.getAnnotation(TypeProperty.class))));
  }

  public static Object getValue(Object object, Field field) {
    try {
      field.setAccessible(true);
      return field.get(object);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new IllegalArgumentException(
          String.format("failed to retrieve value from type[%s], field[%s]", object.getClass().getTypeName(), field.getName()), e);
    }
  }

  @SuppressWarnings("unchecked")
  public static List<Object> getListValue(Object object, Field field) {
    try {
      field.setAccessible(true);
      return (List<Object>) field.get(object);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new IllegalArgumentException(
          String.format("failed to retrieve list value from type[%s], field[%s]", object.getClass().getTypeName(), field.getName()),
          e);
    }
  }

  public static void setValue(Object instance, Object value, Field field) {
    try {
      field.setAccessible(true);
      field.set(instance, value);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new IllegalArgumentException(
          String.format("failed to set value on type[%s], field[%s]", instance.getClass().getTypeName(), field.getName()), e);
    }
  }

}
