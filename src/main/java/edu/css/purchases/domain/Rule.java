package edu.css.purchases.domain;

/** One business rule for a status move. Throws IllegalStateException when the move is forbidden. */
public interface Rule {
  void check(RequestStatus from, RequestStatus to);
}
