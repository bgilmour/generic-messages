package com.langtoun.messages.types.gen.cars;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.PayloadProperty;
import com.langtoun.messages.types.properties.ScalarProperty;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}.
 *
 */
@JsonSerialize(using = PayloadJsonSerializer.class, as = ComplexCar.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = ComplexCar.class)
public class ComplexCar extends SimpleCar {

  private CarEngine engine; // required

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
  public List<PayloadProperty> getProperties() {
    final List<PayloadProperty> properties = super.getProperties();
    properties.add(ScalarProperty.Builder.newBuilder("engine", "engine", "engine", true, CarEngine.class)
        .addGetter(() -> getEngine()).addSetter(o -> setEngine((CarEngine) o)).build());
    return properties;
  }

  @Override
  public String toString() {
    return super.toString() + " [" + engine + "]";
  }

}
