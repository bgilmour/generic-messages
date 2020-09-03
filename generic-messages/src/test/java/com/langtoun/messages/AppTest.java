package com.langtoun.messages;

import java.io.IOException;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langtoun.messages.generic.AwsComplexTypeJsonDeserializer;
import com.langtoun.messages.types.AwsComplexType;
import com.langtoun.messages.types.gen.cars.CarEngine;
import com.langtoun.messages.types.gen.cars.CarFeature;
import com.langtoun.messages.types.gen.cars.ComplexCar;
import com.langtoun.messages.types.gen.cars.ComplexCarWithFeatures;
import com.langtoun.messages.types.gen.cars.CustomComplexCar;
import com.langtoun.messages.types.gen.cars.CustomComplexCarWithFeatures;
import com.langtoun.messages.types.gen.cars.CustomSimpleCar;
import com.langtoun.messages.types.gen.cars.SimpleCar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for generic messages.
 */
public class AppTest extends TestCase {

  private static final ObjectMapper jsonMapper = new ObjectMapper();

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

  public void testCustomCar() throws IOException {

  }

  public void testPlayground() throws IOException {
    System.out.println("---- PLAYGROUND START ----");
    System.out.println();
    System.out.println("---- SERIALIZATION ----");
    final AwsComplexType car1 = new SimpleCar("Blue", "Mazda", null);
    final AwsComplexType car2 = new ComplexCar("Blue", "Mazda", false, new CarEngine(4, "petrol"));
    final AwsComplexType car3 = new ComplexCarWithFeatures("Blue", "Mazda", true, new CarEngine(4, "petrol"));
    ((ComplexCarWithFeatures) car3).addFeature(new CarFeature("19 inch alloys", null));
    ((ComplexCarWithFeatures) car3).addFeature(new CarFeature("Bose sound system", 1200.0));
    final AwsComplexType car4 = new ComplexCarWithFeatures("Blue", "Mazda", true, new CarEngine(4, "petrol"));
    final AwsComplexType engine = new CarEngine(6, "petrol");
    final AwsComplexType feature = new CarFeature("Bose sound system", 1200.0);

    try {
      System.out.println("serialize: car1(" + car1 + ") -> " + jsonMapper.writeValueAsString(car1));
      System.out.println("serialize: car2(" + car2 + ") -> " + jsonMapper.writeValueAsString(car2));
      System.out.println("serialize: car3(" + car3 + ") -> " + jsonMapper.writeValueAsString(car3));
      System.out.println("serialize: car4(" + car4 + ") -> " + jsonMapper.writeValueAsString(car4));
      System.out.println("serialize: engine(" + engine + ") -> " + jsonMapper.writeValueAsString(engine));
      System.out.println("serialize: feature(" + feature + ") -> " + jsonMapper.writeValueAsString(feature));
    } catch (final IOException e) {
      System.out.println("ERROR: " + e.getMessage());
    }

    System.out.println("---- DESERIALIZATION ----");
    final String jsonCar1 = "{\"colour\":\"Blue\",\"type\":\"Mazda\"}";
    final String jsonCar2 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String jsonCar3 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"},\"features\":[{\"name\":\"19 inch alloys\"},{\"name\":\"Bose sound system\",\"price\":1200.0}]}";
    final String jsonCar4 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"},\"features\":[{\"name\":\"19 inch alloys\"},{\"price\":1200.0}]}";
    final String jsonEngine = "{\"cylinders\":6,\"fuelType\":\"petrol\"}";
    final String jsonFeature = "{\"name\":\"Bose sound system\",\"price\":1200.0}";

    try {
      final AwsComplexType deserCar1 = jsonMapper.readValue(jsonCar1, SimpleCar.class);
      final AwsComplexType deserCar2 = jsonMapper.readValue(jsonCar2, ComplexCar.class);
      final AwsComplexType deserCar3 = jsonMapper.readValue(jsonCar3, ComplexCarWithFeatures.class);
      final AwsComplexType deserEngine = jsonMapper.readValue(jsonEngine, CarEngine.class);
      final AwsComplexType deserFeature = jsonMapper.readValue(jsonFeature, CarFeature.class);

      System.out.println("deserialize: jsonCar1(" + jsonCar1 + ") -> " + deserCar1);
      System.out.println("deserialize: jsonCar2(" + jsonCar2 + ") -> " + deserCar2);
      System.out.println("deserialize: jsonCar3(" + jsonCar3 + ") -> " + deserCar3);
      System.out.println("deserialize: jsonEngine(" + jsonEngine + ") -> " + deserEngine);
      System.out.println("deserialize: jsonFeature(" + jsonFeature + ") -> " + deserFeature);
    } catch (final JsonProcessingException e) {
      System.out.println("ERROR: " + e.getMessage());
    }

    System.out.println("---- DESERIALIZATION (ERROR) ----");
    try {
      final AwsComplexType deserFailure = jsonMapper.readValue(jsonCar4, ComplexCarWithFeatures.class);
      System.out.println("deserialize: jsonCar4(" + jsonCar4 + ") -> " + deserFailure);
    } catch (final IllegalStateException e) {
      System.out.println("ERROR: " + e.getMessage());
    }

    System.out.println("---- DESERIALIZATION (DIRECT CALL) ----");
    try {
      final AwsComplexType deserCar5 = AwsComplexTypeJsonDeserializer.deserialize(jsonCar3, ComplexCarWithFeatures.class);
      System.out.println("direct: jsonCar3(" + jsonCar3 + ") -> " + deserCar5);
    } catch (final IllegalStateException e) {
      System.out.println("ERROR: " + e.getMessage());
    }

    System.out.println("---- CUSTOM ENCODING ----");
    final AwsComplexType customCar1 = new CustomSimpleCar("Blue", "Mazda", null);
    final AwsComplexType customCar2 = new CustomComplexCar("Blue", "Mazda", false, new CarEngine(4, "petrol"));
    final AwsComplexType customCar3 = new CustomComplexCarWithFeatures("Blue", "Mazda", true, new CarEngine(4, "petrol"));
    ((CustomComplexCarWithFeatures) customCar3).addFeature(new CarFeature("19 inch alloys", null));
    ((CustomComplexCarWithFeatures) customCar3).addFeature(new CarFeature("Bose sound system", 1200.0));

    System.out.println("encode: customCar1 -> " + customCar1);
    System.out.println("encode: customCar2 -> " + customCar2);
    System.out.println("encode: customCar3 -> " + customCar3);

    System.out.println("---- CUSTOM DECODING ----");
    final String encodedCar1 = "\"<<<colour=\\\"Blue\\\"|type=\\\"Mazda\\\"|rightHandDrive=null>>>\"";
    final String encodedCar2 = "\"<<<colour=\\\"Blue\\\"|type=\\\"Mazda\\\"|rightHandDrive=false|engine=eyJjeWxpbmRlcnMiOjQsImZ1ZWxUeXBlIjoicGV0cm9sIn0>>>\"";

    try {
      final AwsComplexType decodedCar1 = jsonMapper.readValue(encodedCar1, CustomSimpleCar.class);
      final AwsComplexType decodedCar2 = jsonMapper.readValue(encodedCar2, CustomComplexCar.class);

      System.out.println("decode: encodedCar1(" + encodedCar1 + ") -> " + decodedCar1);
      System.out.println("decode: encodedCar2(" + encodedCar2 + ") -> " + decodedCar2);
    } catch (final Exception e) {
      System.out.println("ERROR: " + e.getMessage());
    }

    System.out.println("---- PLAYGROUND STOP ----");
  }

  public void testRegularSerializationTimings() {
    System.out.println("---- TIMING START (SERIALIZATION - REGULAR) ----");
    IntStream.range(0, 3).forEach(loop -> {
      long dataLen = 0;
      final int iters = 1000000;
      int failures = 0;
      final long start = System.nanoTime();
      for (int i = 0; i < iters; i++) {
        final AwsComplexType car = new ComplexCarWithFeatures("Blue", "Mazda", true, new CarEngine(4, "petrol"));
        ((ComplexCarWithFeatures) car).addFeature(new CarFeature("19 inch alloys", (double) i));
        ((ComplexCarWithFeatures) car).addFeature(new CarFeature("Bose sound system", (double) i));
        try {
          dataLen += jsonMapper.writeValueAsString(car).length();
        } catch (JsonProcessingException e) {
          failures++;
        }
      }
      final long stop = System.nanoTime();
      System.out.printf("loop %d: elapsed = %,d ms, iterations = %,d, failures = %,d, length = %,d bytes\n", loop + 1,
          (stop - start) / 1000000L, iters, failures, dataLen);
    });
    System.out.println("---- TIMING STOP (SERIALIZATION - REGULAR) ----");
    System.out.println("---- TIMING START (SERIALIZATION - CUSTOM) ----");
    IntStream.range(0, 3).forEach(loop -> {
      long dataLen = 0;
      final int iters = 1000000;
      int failures = 0;
      final long start = System.nanoTime();
      for (int i = 0; i < iters; i++) {
        final AwsComplexType car = new CustomComplexCarWithFeatures("Blue", "Mazda", true, new CarEngine(4, "petrol"));
        ((CustomComplexCarWithFeatures) car).addFeature(new CarFeature("19 inch alloys", (double) i));
        ((CustomComplexCarWithFeatures) car).addFeature(new CarFeature("Bose sound system", (double) i));
        try {
          dataLen += jsonMapper.writeValueAsString(car).length();
        } catch (JsonProcessingException e) {
          failures++;
        }
      }
      final long stop = System.nanoTime();
      System.out.printf("loop %d: elapsed = %,d ms, iterations = %,d, failures = %,d, length = %,d bytes\n", loop + 1,
          (stop - start) / 1000000L, iters, failures, dataLen);
    });
    System.out.println("---- TIMING STOP (SERIALIZATION - CUSTOM) ----");
  }

  public void testSerializerWithSimpleCar() throws IOException {
    final SimpleCar car = new SimpleCar("Blue", "Mazda", null);

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\"}";
    final String carStr = jsonMapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCar() throws IOException {
    final ComplexCar car = new ComplexCar("Blue", "Mazda", null, new CarEngine(4, "petrol"));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String carStr = jsonMapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndRequiredNull() throws IOException {
    final ComplexCar car = new ComplexCar("Blue", "Mazda", null, new CarEngine(null, "petrol"));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":null,\"fuelType\":\"petrol\"}}";
    final String carStr = jsonMapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndOptionalNull() throws IOException {
    final ComplexCar car = new ComplexCar("Blue", "Mazda", null, new CarEngine(4, null));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4}}";
    final String carStr = jsonMapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndNoFeatures() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", null, new CarEngine(4, null));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4},\"features\":[]}";
    final String carStr = jsonMapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndOneFeature() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", null, new CarEngine(4, null));
    car.addFeature(new CarFeature("19 inch alloys", 1000.0));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4},\"features\":[{\"name\":\"19 inch alloys\",\"price\":1000.0}]}";
    final String carStr = jsonMapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndOneFeatureOptionalNull() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", null, new CarEngine(4, null));
    car.addFeature(new CarFeature("19 inch alloys", null));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4},\"features\":[{\"name\":\"19 inch alloys\"}]}";
    final String carStr = jsonMapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testDeserializerWithSimpleCar() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\"}";
    final String carStr = "Blue Mazda";
    final AwsComplexType car = jsonMapper.readValue(jsonCar, SimpleCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithSimpleCarLHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"rightHandDrive\":false}";
    final String carStr = "Blue Mazda (lhd)";
    final AwsComplexType car = jsonMapper.readValue(jsonCar, SimpleCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithSimpleCarRHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"rightHandDrive\":true}";
    final String carStr = "Blue Mazda (rhd)";
    final AwsComplexType car = jsonMapper.readValue(jsonCar, SimpleCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithComplexCar() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String carStr = "Blue Mazda [4 cyl petrol]";
    final AwsComplexType car = jsonMapper.readValue(jsonCar, ComplexCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithComplexCarLHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"rightHandDrive\":false,\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String carStr = "Blue Mazda (lhd) [4 cyl petrol]";
    final AwsComplexType car = jsonMapper.readValue(jsonCar, ComplexCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithComplexCarRHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"rightHandDrive\":true,\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String carStr = "Blue Mazda (rhd) [4 cyl petrol]";
    final AwsComplexType car = jsonMapper.readValue(jsonCar, ComplexCar.class);

    assertEquals(carStr, car.toString());
  }

}
