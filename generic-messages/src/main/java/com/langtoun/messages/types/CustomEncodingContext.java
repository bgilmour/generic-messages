package com.langtoun.messages.types;

import java.util.ArrayList;
import java.util.List;

/**
 * An object that contains the information required to process a custom object
 * encoding.
 */
public class CustomEncodingContext {

  public static final String CUSTOM_ENCODING_GQL = "gql";
  public static final String CUSTOM_ENCODING_XML = "xml";
  public static final String CUSTOM_ENCODING_XML_URLENCODED = "xml+urlencoded";
  public static final String CUSTOM_ENCODING_JSON = "json";
  public static final String CUSTOM_ENCODING_JSON_URLENCODED = "json+urlencoded";
  public static final String CUSTOM_ENCODING_BASE64 = "gql";

  private String typeEncoding;
  private String prefix;
  private String suffix;
  private List<String> separators;
  private String keyValueSeparator;

  private CustomEncodingContext() {
    // object are created using the builder's static factory and build methods
  }

  public String getTypeEncoding() { return typeEncoding; }

  public String getPrefix() { return prefix; }

  public String getSuffix() { return suffix; }

  public List<String> getSeparators() { return separators; }

  public String getKeyValueSeparator() { return keyValueSeparator; }

  public boolean isInitialised() { return prefix != null || suffix != null || keyValueSeparator != null; }

  public boolean usesCustomEncoder() {
    return isInitialised() || CUSTOM_ENCODING_GQL.equals(typeEncoding);
  }

  @Override
  public String toString() {
    if (isInitialised()) {
      return String.format("encodingContext[pfx=%s, sfx=%s, kv=%s, seps=%s]", prefix, suffix, keyValueSeparator, separators);
    }
    return "encodingContext[not initialised]";
  }

  public static class Builder {

    private String typeEncoding;
    private String prefix;
    private String suffix;
    private List<String> separators;
    private String keyValueSeparator;

    private Builder() {
      this.separators = new ArrayList<>();
    }

    public static Builder newBuilder() {
      return new Builder();
    }

    public Builder typeEncoding(String typeEncoding) {
      this.typeEncoding = typeEncoding;
      return this;
    }

    public Builder prefix(final String prefix) {
      this.prefix = prefix;
      return this;
    }

    public Builder suffix(final String suffix) {
      this.suffix = suffix;
      return this;
    }

    public Builder separator(final String separator) {
      this.separators.add(separator);
      return this;
    }

    public Builder separators(final List<String> separators) {
      this.separators.addAll(separators);
      return this;
    }

    public Builder keyValueSeparator(final String keyValueSeparator) {
      this.keyValueSeparator = keyValueSeparator;
      return this;
    }

    public CustomEncodingContext build() {
      final CustomEncodingContext context = new CustomEncodingContext();
      context.typeEncoding = this.typeEncoding;
      context.prefix = this.prefix;
      context.suffix = this.suffix;
      context.separators = new ArrayList<>();
      context.separators.addAll(this.separators);
      context.keyValueSeparator = this.keyValueSeparator;
      return context;
    }
  }

}
