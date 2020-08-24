package com.langtoun.messages;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.langtoun.messages.generic.Message;
import com.langtoun.messages.types.CarEngine;
import com.langtoun.messages.types.CarFeature;
import com.langtoun.messages.types.ComplexCar;
import com.langtoun.messages.types.ComplexCarWithFeatures;
import com.langtoun.messages.types.SimpleCar;

/**
 * Hello world!
 *
 */
public class App {

  public static void main(String[] args) {
    System.out.println("Running com.langtoun.messages.App ...");

    SimpleCar car1 = new SimpleCar("Blue", "Mazda");
    ComplexCar car2 = new ComplexCar("Blue", "Mazda", new CarEngine(4, "petrol"));
    ComplexCarWithFeatures car3 = new ComplexCarWithFeatures("Blue", "Mazda", new CarEngine(4, "petrol"));
    car3.addFeature(new CarFeature("19 inch alloys", null));
    car3.addFeature(new CarFeature("Bose sound system", 1200.0));

    Message<SimpleCar> message1 = Message.newMessage(car1);
    Message<ComplexCar> message2 = Message.newMessage(car2);
    Message<ComplexCarWithFeatures> message3 = Message.newMessage(car3);

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      System.out.println("simple(" + car1 + ") : " + objectMapper.writeValueAsString(message1));
      System.out.println("complex(" + car2 + ") : " + objectMapper.writeValueAsString(message2));
      System.out.println("complexWithFeatures(" + car3 + ") : " + objectMapper.writeValueAsString(message3));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
