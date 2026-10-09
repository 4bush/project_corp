package edu.css.purchases.domain;

/** Unchecked on purpose: it makes the surrounding transaction roll back. */
public class DuplicateRequest extends RuntimeException {
  public DuplicateRequest(String businessKey) {
    super("duplicate purchase request: " + businessKey);
  }
}
