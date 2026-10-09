package edu.css.purchases.domain;

import java.util.UUID;

/** Surrogate id of a purchase request. Null or blank is rejected. */
public record RequestId(String value) {

  public RequestId {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("request id must not be blank");
    }
  }

  public static RequestId newId() {
    return new RequestId(UUID.randomUUID().toString());
  }

  public UUID uuid() {
    return UUID.fromString(value);
  }
}
