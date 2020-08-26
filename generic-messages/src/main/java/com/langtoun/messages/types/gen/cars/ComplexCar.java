package com.langtoun.messages.types.gen.cars;

import static com.langtoun.messages.types.properties.ScalarProperty.newScalarProperty;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.MessageJsonDeserializer;
import com.langtoun.messages.generic.MessageJsonSerializer;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.MessageProperty;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}.
 *
 */
@JsonSerialize(using = MessageJsonSerializer.class, as = ComplexCar.class)
@JsonDeserialize(using = MessageJsonDeserializer.class, as = ComplexCar.class)
public class ComplexCar extends SimpleCar {

  private CarEngine engine; // required

  public ComplexCar() {

  }

  public ComplexCar(final String colour, final String type, final CarEngine engine) {
    super(colour, type);
    this.engine = engine;
  }

  public CarEngine getEngine() { return engine; }

  public void setEngine(final CarEngine engine) { this.engine = engine; }

  @Override
  public List<MessageProperty> getProperties() {
    final List<MessageProperty> properties = super.getProperties();
    properties.add(
        newScalarProperty("engine", "engine", "engine", true, () -> getEngine(), o -> setEngine((CarEngine) o), CarEngine.class));
    return properties;
  }

  @Override
  public String toString() {
    return super.toString() + " [" + engine + "]";
  }

}
