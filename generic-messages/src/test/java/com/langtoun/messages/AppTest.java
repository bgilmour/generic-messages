package com.langtoun.messages;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langtoun.messages.types.AwsComplexType;
import com.langtoun.messages.types.gen.cars.CarEngine;
import com.langtoun.messages.types.gen.cars.CarFeature;
import com.langtoun.messages.types.gen.cars.ComplexCar;
import com.langtoun.messages.types.gen.cars.ComplexCarWithFeatures;
import com.langtoun.messages.types.gen.cars.SimpleCar;

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

  public void testCustomCar() throws IOException {

  }

  public void testPlayground() throws IOException {
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
      System.out.println("serialize: car1(" + car1 + ") -> " + mapper.writeValueAsString(car1));
      System.out.println("serialize: car2(" + car2 + ") -> " + mapper.writeValueAsString(car2));
      System.out.println("serialize: car3(" + car3 + ") -> " + mapper.writeValueAsString(car3));
      System.out.println("serialize: car4(" + car4 + ") -> " + mapper.writeValueAsString(car4));
      System.out.println("serialize: engine(" + engine + ") -> " + mapper.writeValueAsString(engine));
      System.out.println("serialize: feature(" + feature + ") -> " + mapper.writeValueAsString(feature));
    } catch (final IOException e) {
      e.printStackTrace();
    }

    System.out.println("---- DESERIALIZATION ----");
    final String jsonCar1 = "{\"colour\":\"Blue\",\"type\":\"Mazda\"}";
    final String jsonCar2 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cyls\":4,\"fuel\":\"petrol\"}}";
    final String jsonCar3 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cyls\":4,\"fuel\":\"petrol\"},\"features\":[{\"name\":\"19 inch alloys\"},{\"name\":\"Bose sound system\",\"price\":1200.0}]}";
    final String jsonCar4 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cyls\":4,\"fuel\":\"petrol\"},\"features\":[{\"name\":\"19 inch alloys\"},{\"price\":1200.0}]}";
    final String jsonEngine = "{\"cyls\":6,\"fuel\":\"petrol\"}";
    final String jsonFeature = "{\"name\":\"Bose sound system\",\"price\":1200.0}";

    try {
      final AwsComplexType decMessage1 = mapper.readValue(jsonCar1, SimpleCar.class);
      final AwsComplexType decMessage2 = mapper.readValue(jsonCar2, ComplexCar.class);
      final AwsComplexType decMessage3 = mapper.readValue(jsonCar3, ComplexCarWithFeatures.class);
      final AwsComplexType decMessage4 = mapper.readValue(jsonEngine, CarEngine.class);
      final AwsComplexType decMessage5 = mapper.readValue(jsonFeature, CarFeature.class);

      System.out.println("deserialize: jsonCar1(" + jsonCar1 + ") -> " + decMessage1);
      System.out.println("deserialize: jsonCar2(" + jsonCar2 + ") -> " + decMessage2);
      System.out.println("deserialize: jsonCar3(" + jsonCar3 + ") -> " + decMessage3);
      System.out.println("deserialize: jsonEngine(" + jsonEngine + ") -> " + decMessage4);
      System.out.println("deserialize: jsonFeature(" + jsonFeature + ") -> " + decMessage5);
    } catch (final JsonProcessingException e) {
      e.printStackTrace();
    }

    try {
      final AwsComplexType decFailure = mapper.readValue(jsonCar4, ComplexCarWithFeatures.class);
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

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cyls\":4,\"fuel\":\"petrol\"}}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndRequiredNull() throws IOException {
    final ComplexCar car = new ComplexCar("Blue", "Mazda", null, new CarEngine(null, "petrol"));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cyls\":null,\"fuel\":\"petrol\"}}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndOptionalNull() throws IOException {
    final ComplexCar car = new ComplexCar("Blue", "Mazda", null, new CarEngine(4, null));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cyls\":4}}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndNoFeatures() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", null, new CarEngine(4, null));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cyls\":4},\"features\":[]}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndOneFeature() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", null, new CarEngine(4, null));
    car.addFeature(new CarFeature("19 inch alloys", 1000.0));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cyls\":4},\"features\":[{\"name\":\"19 inch alloys\",\"price\":1000.0}]}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithComplexCarAndOneFeatureOptionalNull() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", null, new CarEngine(4, null));
    car.addFeature(new CarFeature("19 inch alloys", null));

    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cyls\":4},\"features\":[{\"name\":\"19 inch alloys\"}]}";
    final String carStr = mapper.writeValueAsString(car);

    assertEquals(jsonCar, carStr);
  }

  public void testDeserializerWithSimpleCar() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\"}";
    final String carStr = "Blue Mazda";
    final AwsComplexType car = mapper.readValue(jsonCar, SimpleCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithSimpleCarLHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"rhs\":false}";
    final String carStr = "Blue Mazda (lhd)";
    final AwsComplexType car = mapper.readValue(jsonCar, SimpleCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithSimpleCarRHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"rhs\":true}";
    final String carStr = "Blue Mazda (rhd)";
    final AwsComplexType car = mapper.readValue(jsonCar, SimpleCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithComplexCar() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cyls\":4,\"fuel\":\"petrol\"}}";
    final String carStr = "Blue Mazda [4 cyl petrol]";
    final AwsComplexType car = mapper.readValue(jsonCar, ComplexCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithComplexCarLHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"rhs\":false,\"engine\":{\"cyls\":4,\"fuel\":\"petrol\"}}";
    final String carStr = "Blue Mazda (lhd) [4 cyl petrol]";
    final AwsComplexType car = mapper.readValue(jsonCar, ComplexCar.class);

    assertEquals(carStr, car.toString());
  }

  public void testDeserializerWithComplexCarRHD() throws IOException {
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"rhs\":true,\"engine\":{\"cyls\":4,\"fuel\":\"petrol\"}}";
    final String carStr = "Blue Mazda (rhd) [4 cyl petrol]";
    final AwsComplexType car = mapper.readValue(jsonCar, ComplexCar.class);

    assertEquals(carStr, car.toString());
  }

}
