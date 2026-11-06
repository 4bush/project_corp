package edu.css.purchases.domain;

public class RequestNotFoundException extends RuntimeException {
  public RequestNotFoundException(String id) {
    super("purchase request not found: " + id);
  }
}
