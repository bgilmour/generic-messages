package com.langtoun.messages;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langtoun.messages.generic.Message;
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

  public void testSerializeCarWithObjectMapper() throws JsonProcessingException {
    final ObjectMapper objectMapper = new ObjectMapper();
    final SimpleCar car = new SimpleCar("Yellow", "Renault");
    final String jsonCar = "{\"colour\":\"Yellow\",\"type\":\"Renault\",\"properties\":[{\"name\":\"colour\",\"jsonName\":\"colour\",\"xmlName\":\"colour\",\"required\":true,\"valueType\":\"java.lang.String\",\"getter\":{},\"setter\":{}},{\"name\":\"type\",\"jsonName\":\"type\",\"xmlName\":\"type\",\"required\":true,\"valueType\":\"java.lang.String\",\"getter\":{},\"setter\":{}}]}";
    final String carStr = objectMapper.writeValueAsString(car);
    System.out.println(carStr);

    assertEquals(jsonCar, carStr);
  }

  public void testDeserializeSimpleCarWithObjectMapperUsingClass() throws JsonProcessingException {
    final ObjectMapper objectMapper = new ObjectMapper();
    final Class<?> clazz = SimpleCar.class;
    final String json = "{ \"colour\" : \"Black\", \"type\" : \"BMW\" }";
    final Object car = objectMapper.readValue(json, clazz);

    assertEquals("Black BMW", car.toString());
  }

  public void testDeserializeSimpleCarWithObjectMapperUsingTypeReference() throws JsonProcessingException {
    final ObjectMapper objectMapper = new ObjectMapper();
    final TypeReference<?> typeRef = new TypeReference<SimpleCar>() {
      // no implementation
    };
    final String json = "{ \"colour\" : \"White\", \"type\" : \"BMW\" }";
    final Object car = objectMapper.readValue(json, typeRef);

    assertEquals("White BMW", car.toString());
  }

  public void testSerializerWithMessageSimpleCar() throws IOException {
    final SimpleCar car = new SimpleCar("Blue", "Mazda");
    final Message<SimpleCar> message = Message.from(car);

    final ObjectMapper objectMapper = new ObjectMapper();
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\"}";
    final String carStr = objectMapper.writeValueAsString(message);
    System.out.println("simple(" + car + ") : " + carStr);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithMessageComplexCar() throws IOException {
    final ComplexCar car = new ComplexCar("Blue", "Mazda", new CarEngine(4, "petrol"));
    final Message<ComplexCar> message = Message.from(car);

    final ObjectMapper objectMapper = new ObjectMapper();
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String carStr = objectMapper.writeValueAsString(message);
    System.out.println("complex(" + car + ") : " + carStr);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithMessageComplexCarAndRequiredNull() throws IOException {
    final ComplexCar car = new ComplexCar("Blue", "Mazda", new CarEngine(null, "petrol"));
    final Message<ComplexCar> message = Message.from(car);

    final ObjectMapper objectMapper = new ObjectMapper();
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":null,\"fuelType\":\"petrol\"}}";
    final String carStr = objectMapper.writeValueAsString(message);
    System.out.println("complex(" + car + ") : " + carStr);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithMessageComplexCarAndOptionalNull() throws IOException {
    final ComplexCar car = new ComplexCar("Blue", "Mazda", new CarEngine(4, null));
    final Message<ComplexCar> message = Message.from(car);

    final ObjectMapper objectMapper = new ObjectMapper();
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4}}";
    final String carStr = objectMapper.writeValueAsString(message);
    System.out.println("complex(" + car + ") : " + carStr);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithMessageComplexCarAndNoFeatures() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", new CarEngine(4, null));
    final Message<ComplexCarWithFeatures> message = Message.from(car);

    final ObjectMapper objectMapper = new ObjectMapper();
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4}}";
    final String carStr = objectMapper.writeValueAsString(message);
    System.out.println("complex(" + car + ") : " + carStr);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithMessageComplexCarAndOneFeature() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", new CarEngine(4, null));
    car.addFeature(new CarFeature("19 inch alloys", 1000.0));
    final Message<ComplexCarWithFeatures> message = Message.from(car);

    final ObjectMapper objectMapper = new ObjectMapper();
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4},\"features\":[{\"name\":\"19 inch alloys\",\"price\":1000.0}]}";
    final String carStr = objectMapper.writeValueAsString(message);
    System.out.println("complex(" + car + ") : " + carStr);

    assertEquals(jsonCar, carStr);
  }

  public void testSerializerWithMessageComplexCarAndOneFeatureOptionalNull() throws IOException {
    final ComplexCarWithFeatures car = new ComplexCarWithFeatures("Blue", "Mazda", new CarEngine(4, null));
    car.addFeature(new CarFeature("19 inch alloys", null));
    final Message<ComplexCarWithFeatures> message = Message.from(car);

    final ObjectMapper objectMapper = new ObjectMapper();
    final String jsonCar = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4},\"features\":[{\"name\":\"19 inch alloys\"}]}";
    final String carStr = objectMapper.writeValueAsString(message);
    System.out.println("complex(" + car + ") : " + carStr);

    assertEquals(jsonCar, carStr);
  }

}
