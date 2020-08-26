package com.langtoun.messages;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.gen.cars.CarEngine;
import com.langtoun.messages.types.gen.cars.CarFeature;
import com.langtoun.messages.types.gen.cars.ComplexCar;
import com.langtoun.messages.types.gen.cars.ComplexCarWithFeatures;
import com.langtoun.messages.types.gen.cars.SimpleCar;

/**
 * App featuring generic (de-)serializable message with de-coupled payload
 * types.
 *
 */
public class App {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static void main(final String[] args) {
    System.out.println("Running com.langtoun.messages.App ...");

    System.out.println("---- SERIALIZATION ----");
    final SerializablePayload car1 = new SimpleCar("Blue", "Mazda");
    final SerializablePayload car2 = new ComplexCar("Blue", "Mazda", new CarEngine(4, "petrol"));
    final SerializablePayload car3 = new ComplexCarWithFeatures("Blue", "Mazda", new CarEngine(4, "petrol"));
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

    System.out.println("---- DESERIALIZATION ----");
    final String jsonCar1 = "{\"colour\":\"Blue\",\"type\":\"Mazda\"}";
    final String jsonCar2 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String jsonCar3 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"},\"features\":[{\"name\":\"19 inch alloys\"},{\"name\":\"Bose sound system\",\"price\":1200.0}]}";
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
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

}
