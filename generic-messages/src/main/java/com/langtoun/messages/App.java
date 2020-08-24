package com.langtoun.messages;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langtoun.messages.generic.Message;
import com.langtoun.messages.types.cars.CarEngine;
import com.langtoun.messages.types.cars.CarFeature;
import com.langtoun.messages.types.cars.ComplexCar;
import com.langtoun.messages.types.cars.ComplexCarWithFeatures;
import com.langtoun.messages.types.cars.SimpleCar;

/**
 * App featuring eneric (de-)serializable message with de-coupled payload types.
 *
 */
public class App {

  public static void main(final String[] args) {
    System.out.println("Running com.langtoun.messages.App ...");

    final SimpleCar car1 = new SimpleCar("Blue", "Mazda");
    final ComplexCar car2 = new ComplexCar("Blue", "Mazda", new CarEngine(4, "petrol"));
    final ComplexCarWithFeatures car3 = new ComplexCarWithFeatures("Blue", "Mazda", new CarEngine(4, "petrol"));
    car3.addFeature(new CarFeature("19 inch alloys", null));
    car3.addFeature(new CarFeature("Bose sound system", 1200.0));

    final Message<SimpleCar> encMessage1 = Message.from(car1);
    final Message<ComplexCar> encMessage2 = Message.from(car2);
    final Message<ComplexCarWithFeatures> encMessage3 = Message.from(car3);

    final ObjectMapper mapper = new ObjectMapper();

    try {
      System.out.println("serialize: car1(" + car1 + ") -> " + mapper.writeValueAsString(encMessage1));
      System.out.println("serialize: car2(" + car2 + ") -> " + mapper.writeValueAsString(encMessage2));
      System.out.println("serialize: car3(" + car3 + ") -> " + mapper.writeValueAsString(encMessage3));
    } catch (final IOException e) {
      e.printStackTrace();
    }

    final String jsonCar1 = "{\"colour\":\"Blue\",\"type\":\"Mazda\"}";
    final String jsonCar2 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"}}";
    final String jsonCar3 = "{\"colour\":\"Blue\",\"type\":\"Mazda\",\"engine\":{\"cylinders\":4,\"fuelType\":\"petrol\"},\"features\":[{\"name\":\"19 inch alloys\"},{\"name\":\"Bose sound system\",\"price\":1200.0}]}";

    Message<SimpleCar> decMessage1 = Message.from(jsonCar1, new TypeReference<Message<SimpleCar>>() {
    });
//    Message<ComplexCar> decMessage2 = Message.from(jsonCar2, ComplexCar.class);
//    Message<ComplexCarWithFeatures> decMessage3 = Message.from(jsonCar3, ComplexCarWithFeatures.class);

    System.out.println("deserialize: jsonCar1(" + jsonCar1 + ") -> " + decMessage1.getPayload());
//    System.out.println("deserialize: jsonCar2(" + jsonCar2 + ") -> " + decMessage2.getPayload());
//    System.out.println("deserialize: jsonCar3(" + jsonCar3 + ") -> " + decMessage3.getPayload());
  }

}
