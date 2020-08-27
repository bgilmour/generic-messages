package com.langtoun.messages.types.gen.cars;

import static com.langtoun.messages.types.properties.ListProperty.newListProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.PayloadProperty;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}.
 *
 */
@JsonSerialize(using = PayloadJsonSerializer.class, as = ComplexCarWithFeatures.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = ComplexCarWithFeatures.class)
public class ComplexCarWithFeatures extends ComplexCar {

  private final List<CarFeature> features = new ArrayList<>();

  public ComplexCarWithFeatures() {
    // TODO Auto-generated constructor stub
  }

  public ComplexCarWithFeatures(final String colour, final String type, final Boolean rightHandDrive, final CarEngine engine) {
    super(colour, type, rightHandDrive, engine);
    // TODO Auto-generated constructor stub
  }

  public List<CarFeature> getFeatures() { return features; }

  public void setFeatures(final List<CarFeature> features) {
    this.features.addAll(features);
  }

  public void addFeature(final CarFeature feature) {
    features.add(feature);
  }

  @Override
  public List<PayloadProperty> getProperties() {
    final List<PayloadProperty> properties = super.getProperties();
    properties.add(newListProperty("features", "features", "features", false,
        () -> features != null ? features.stream().map(o -> (Object) o).collect(Collectors.toList()) : new ArrayList<>(),
        l -> l.stream().map(o -> (CarFeature) o).forEach(o -> features.add(o)), List.class, CarFeature.class));
    return properties;
  }

  @Override
  public String toString() {
    return super.toString() + " with " + features + "]";
  }

}
