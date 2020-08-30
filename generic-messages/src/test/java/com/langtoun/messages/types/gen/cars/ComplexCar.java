package com.langtoun.messages.types.gen.cars;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.annotations.TypeProperty;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;
import com.langtoun.messages.types.SerializablePayload;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}.
 *
 */
@JsonSerialize(using = PayloadJsonSerializer.class, as = ComplexCar.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = ComplexCar.class)
public class ComplexCar extends SimpleCar {

  @TypeProperty(required = true, jsonName = "engine")
  private CarEngine engine;

  public ComplexCar() {
    // do nothing
  }

  public ComplexCar(final String colour, final String type, final Boolean rightHandDrive, final CarEngine engine) {
    super(colour, type, rightHandDrive);
    this.engine = engine;
  }

  public CarEngine getEngine() { return engine; }

  public void setEngine(final CarEngine engine) { this.engine = engine; }

  @Override
  public String toString() {
    return super.toString() + " [" + engine + "]";
  }

}
