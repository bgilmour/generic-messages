package com.langtoun.messages.types.gen.cars;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.ListProperty;
import com.langtoun.messages.types.properties.PayloadProperty;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}.
 *
 */
@JsonSerialize(using = PayloadJsonSerializer.class, as = ComplexCarWithFeatures.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = ComplexCarWithFeatures.class)
public class ComplexCarWithFeatures extends ComplexCar {

  private final List<CarFeature> features = new ArrayList<>(); // optional

  public ComplexCarWithFeatures() {
    // do nothing
  }

  public ComplexCarWithFeatures(final String colour, final String type, final Boolean rightHandDrive, final CarEngine engine) {
    super(colour, type, rightHandDrive, engine);
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
    properties.add(ListProperty.Builder.newBuilder("features", "features", "features", false)
        .addGetter(() -> features != null ? features.stream().map(o -> (Object) o).collect(Collectors.toList()) : new ArrayList<>())
        .addSetter(l -> l.stream().map(o -> (CarFeature) o).forEach(o -> features.add(o))).addItemType(CarFeature.class).build());
    return properties;
  }

  @Override
  public String toString() {
    return super.toString() + " with " + features + "]";
  }

}
