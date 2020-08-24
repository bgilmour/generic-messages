package com.langtoun.messages.types;

import static com.langtoun.messages.properties.ScalarProperty.newScalarProperty;

import java.util.List;

import com.langtoun.messages.properties.MessageProperty;

public class ComplexCar extends SimpleCar {

  private CarEngine engine; // required

  public ComplexCar() {

  }

  public ComplexCar(final String colour, final String type, final CarEngine engine) {
    super(colour, type);
    this.engine = engine;
  }

  public CarEngine getEngine() {
    return engine;
  }

  public void setEngine(final CarEngine engine) {
    this.engine = engine;
  }

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
