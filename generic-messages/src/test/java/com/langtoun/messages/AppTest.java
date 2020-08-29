package com.langtoun.messages;

import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_BASE64;
//import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_GQL;
import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_JSON;
import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_JSON_URLENCODED;
//import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_XML;
//import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_XML_URLENCODED;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langtoun.messages.types.CustomEncodingContext;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.gen.cars.CarEngine;
import com.langtoun.messages.types.gen.cars.CarFeature;
import com.langtoun.messages.types.gen.cars.ComplexCar;
import com.langtoun.messages.types.gen.cars.ComplexCarWithFeatures;
import com.langtoun.messages.types.gen.cars.SimpleCar;
import com.langtoun.messages.types.properties.ListProperty;
import com.langtoun.messages.types.properties.ScalarProperty;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for generic messages.
 */
public class AppTest extends TestCase {

  private static final ObjectMapper mapper = new ObjectMapper();

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public AppTest(final String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  public void testPlayground() throws IOException {
    System.out.println("---- PROPERTIES (SCALAR) ----");
    try {
      final ScalarProperty.Builder builder1 = ScalarProperty.Builder.newBuilder("a", "b", "c", true, String.class);
      builder1.addGetter(() -> {
        return null;
      });
      builder1.addSetter(o -> {});
      System.out.println("INFO : builder1: " + builder1.build());
    } catch (final RuntimeException e) {
      System.out.println("ERROR: builder1: " + e);
    }
    try {
      final ScalarProperty.Builder builder2 = ScalarProperty.Builder.newBuilder("a", "b", "c", true, String.class);
      builder2.addSetter(o -> {});
      System.out.println("INFO : builder2: " + builder2.build());
    } catch (final RuntimeException e) {
      System.out.println("ERROR: builder2: " + e);
    }
    try {
      final ScalarProperty.Builder builder3 = ScalarProperty.Builder.newBuilder("a", "b", "c", true, String.class);
      builder3.addGetter(() -> {
        return null;
      });
      System.out.println("INFO : builder3: " + builder3.build());
    } catch (final RuntimeException e) {
      System.out.println("ERROR: builder3: " + e);
    }

    System.out.println("---- PROPERTIES (LIST) ----");
    try {
      final ListProperty.Builder builder1 = ListProperty.Builder.newBuilder("a", "b", "c", true);
      builder1.getter(() -> {
        return null;
      });
      builder1.setter(o -> {});
      builder1.itemType(String.class);
      System.out.println("INFO : builder1: " + builder1.build());
    } catch (final RuntimeException e) {
      System.out.println("ERROR: builder1: " + e);
    }
    try {
      final ListProperty.Builder builder2 = ListProperty.Builder.newBuilder("a", "b", "c", true);
      builder2.setter(o -> {});
      builder2.itemType(String.class);
      System.out.println("INFO : builder2: " + builder2.build());
    } catch (final RuntimeException e) {
      System.out.println("ERROR: builder2: " + e);
    }
    try {
      final ListProperty.Builder builder3 = ListProperty.Builder.newBuilder("a", "b", "c", true);
      builder3.getter(() -> {
        return null;
      });
      builder3.itemType(String.class);
      System.out.println("INFO : builder3: " + builder3.build());
    } catch (final RuntimeException e) {
      System.out.println("ERROR: builder3: " + e);
    }
    try {
      final ListProperty.Builder builder4 = ListProperty.Builder.newBuilder("a", "b", "c", true);
      builder4.getter(() -> {
        return null;
      });
      builder4.setter(o -> {});
      System.out.println("INFO : builder4: " + builder4.build());
    } catch (final RuntimeException e) {
      System.out.println("ERROR: builder4: " + e);
    }

    System.out.println("---- SERIALIZATION ----");
    final SerializablePayload car1 = new SimpleCar("Blue", "Mazda", null);
    final SerializablePayload car2 = new ComplexCar("Blue", "Mazda", false, new CarEngine(4, "petrol"));
    final SerializablePayload car3 = new ComplexCarWithFeatures("Blue", "Mazda", true, new CarEngine(4, "petrol"));
    ((ComplexCarWithFeatures) car3).addFeature(new CarFeature("19 inch alloys", null));
    ((ComplexCarWithFeatures) car3).addFeature(new CarFeature("Bose sound system", 1200.0));
    final SerializablePayload engine = new CarEngine(6, "petrol");
    final SerializablePayload feature = new CarFeature("Bose sound system", 1200.0);

    try {
      System.out.println("serialize: car1(" + car1 + ") -> " + mapper.writeValueAsString(car1));
      System.out.println("serialize: car2(" + car2 + ") -> " + mapper.writeValueAsString(car2));
      System.out.println("serialize: car3(" + car3 + ") -> " + mapper.writeValueAsString(car3));
      System.out.println("serialize: engine(" + engine + ") -> " + mapper.writeValueAsString(engine));
      System.out.println("serialize: feature(" + feature + ") -> " + mapper.writeValueAsString(feature));
    } catch (final IOException e) {
      e.printStackTrace();
    }

    System.out.println("---- SERIALIZATION (CUSTOM ENCODING) ----");
    final SimpleCar customCar1 = new SimpleCar("Blue", "Mazda", null);
    final ComplexCar customCar2 = new ComplexCar("Blue", "Mazda", false, new CarEngine(4, "petrol"));
    final ComplexCarWithFeatures customCar3 = new ComplexCarWithFeatures("Blue", "Mazda", true, new CarEngine(4, "petrol"));
    customCar3.addFeature(new CarFeature("19 inch alloys", null));
    customCar3.addFeature(new CarFeature("Bose sound system", 1200.0));

    final CustomEncodingContext.Builder context1 = CustomEncodingContext.Builder.newBuilder().prefix("<<<").suffix(">>>")
        .separator("**");
    final CustomEncodingContext.Builder context2 = CustomEncodingContext.Builder.newBuilder().prefix("<<<").suffix(">>>")
        .separator("**").keyValueSeparator("->");
    final CustomEncodingContext.Builder context3 = CustomEncodingContext.Builder.newBuilder();

    customCar1.setCustomEncodingContext(context1.build());
    customCar2.getEngine().setCustomEncodingContext(context1.build());
    customCar3.getFeatures().get(1).setCustomEncodingContext(context2.build());

    System.out.println("encoding: context1 -> " + context1);
    System.out.println("encoding: context2 -> " + context2);
    System.out.println("encoding: context3 -> " + context3);

    System.out.println("custom: car1 -> " + customCar1);
    System.out.println("custom: car2 (engine) -> " + customCar2);
    System.out.println("custom: car3 (feature[1]) -> " + customCar3);

    customCar2.getEngine().setCustomEncodingContext(null);
    customCar2.setPropertyTypeEncoding(3, CUSTOM_ENCODING_JSON);
    System.out.println("custom: car2 (custom,json) -> " + customCar2);
    customCar2.setPropertyTypeEncoding(3, CUSTOM_ENCODING_JSON_URLENCODED);
    System.out.println("custom: car2 (custom,json+urlencoded) -> " + customCar2);
    customCar2.setPropertyTypeEncoding(3, CUSTOM_ENCODING_BASE64);
    System.out.println("custom: car2 (custom,base64) -> " + customCar2);

    System.out.println("---- DESERIALIZATION ----");
    final String jsonCar1 = "{\"colour\":\"Blue\",\"type\":\"Mazda\"}";
    final String jsonCar2 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String jsonCar3 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"},\"features\":[{\"name\":\"19 inch alloys\"},{\"name\":\"Bose sound system\",\"price\":1200.0}]}";
    final String jsonCar4 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"},\"features\":[{\"name\":\"19 inch alloys\"},{\"price\":1200.0}]}";
    final String jsonEngine = "{\"cylinders\":6,\"fuelType\":\"petrol\"}";
    final String jsonFeature = "{\"name\":\"Bose sound system\",\"price\":1200.0}";

    try {
      final SerializablePayload decMessage1 = mapper.readValue(jsonCar1, SimpleCar.class);
      final SerializablePayload decMessage2 = mapper.readValue(jsonCar2, ComplexCar.class);
      final SerializablePayload decMessage3 = mapper.readValue(jsonCar3, ComplexCarWithFeatures.class);
      final SerializablePayload decMessage4 = mapper.readValue(jsonEngine, CarEngine.class);
      final SerializablePayload decMessage5 = mapper.readValue(jsonFeature, CarFeature.class);

      System.out.println("deserialize: jsonCar1(" + jsonCar1 + ") -> " + decMessage1);
      System.out.println("deserialize: jsonCar2(" + jsonCar2 + ") -> " + decMessage2);
      System.out.println("deserialize: jsonCar3(" + jsonCar3 + ") -> " + decMessage3);
      System.out.println("deserialize: jsonEngine(" + jsonEngine + ") -> " + decMessage4);
      System.out.println("deserialize: jsonFeature(" + jsonFeature + ") -> " + decMessage5);
    } catch (final JsonProcessingException e) {
      e.printStackTrace();
    }

    try {
      final SerializablePayload decFailure = mapper.readValue(jsonCar4, ComplexCarWithFeatures.class);
      System.out.println("deserialize: jsonCar4(" + jsonCar4 + ") -> " + decFailure);
    } catch (final IllegalStateException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  public void testSerializerWithSimpleCar() throws IOException {
    final SimpleCar car = new SimpleCar("Blue", "Mazda", null);

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\"}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCar() throws IOException {
    final ComplexCar car = new ComplexCar("Blue", "Mazda", null, new CarEngine(4, "petrol"));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndRequiredNull() throws IOException {
    final ComplexCar car = new ComplexCar("Blue", "Mazda", null, new CarEngine(null, "petrol"));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":null,\"fuelType\":\"petrol\"}}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndOptionalNull() throws IOException {
    final ComplexCar car = new ComplexCar("Blue", "Mazda", null, new CarEngine(4, null));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4}}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndNoFeatures() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", null, new CarEngine(4, null));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4}}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndOneFeature() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", null, new CarEngine(4, null));
    car.addFeature(new CarFeature("19 inch alloys", 1000.0));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4},\"features\":[{\"name\":\"19 inch alloys\",\"price\":1000.0}]}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndOneFeatureOptionalNull() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", null, new CarEngine(4, null));
    car.addFeature(new CarFeature("19 inch alloys", null));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4},\"features\":[{\"name\":\"19 inch alloys\"}]}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testDeserializerWithSimpleCar() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\"}";
    final String carStr = "Blue Mazda";
    final SerializablePayload car = mapper.readValue(jsonCar, SimpleCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithSimpleCarLHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"right_hand_drive\":false}";
    final String carStr = "Blue Mazda (lhd)";
    final SerializablePayload car = mapper.readValue(jsonCar, SimpleCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithSimpleCarRHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"right_hand_drive\":true}";
    final String carStr = "Blue Mazda (rhd)";
    final SerializablePayload car = mapper.readValue(jsonCar, SimpleCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithComplexCar() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String carStr = "Blue Mazda [4 cyl petrol]";
    final SerializablePayload car = mapper.readValue(jsonCar, ComplexCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithComplexCarLHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"right_hand_drive\":false,\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String carStr = "Blue Mazda (lhd) [4 cyl petrol]";
    final SerializablePayload car = mapper.readValue(jsonCar, ComplexCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithComplexCarRHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"right_hand_drive\":true,\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String carStr = "Blue Mazda (rhd) [4 cyl petrol]";
    final SerializablePayload car = mapper.readValue(jsonCar, ComplexCar.class);

    assertEquals(carStr, car.toString());
  }

}
