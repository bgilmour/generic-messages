package com.langtoun.messages;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.langtoun.messages.generic.Message;
import com.langtoun.messages.types.cars.CarEngine;
import com.langtoun.messages.types.cars.CarFeature;
import com.langtoun.messages.types.cars.ComplexCar;
import com.langtoun.messages.types.cars.ComplexCarWithFeatures;
import com.langtoun.messages.types.cars.SimpleCar;

/**
 * Generic (de-)serializable message with de-coupled payload types.
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

    final Message<SimpleCar> message1 = Message.newMessage(car1);
    final Message<ComplexCar> message2 = Message.newMessage(car2);
    final Message<ComplexCarWithFeatures> message3 = Message.newMessage(car3);

    final ObjectMapper objectMapper = new ObjectMapper();
    try {
      System.out.println("simple(" + car1 + ") : " + objectMapper.writeValueAsString(message1));
      System.out.println("complex(" + car2 + ") : " + objectMapper.writeValueAsString(message2));
      System.out.println("complexWithFeatures(" + car3 + ") : " + objectMapper.writeValueAsString(message3));
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

}
