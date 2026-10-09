package edu.css.purchases.domain;

/** Allowed -> returns the new status. Forbidden -> IllegalStateException. */
public class RequestPolicy {

  private final Rule rules;

  public RequestPolicy(Rule rules) {
    this.rules = rules;
  }

  public RequestStatus move(RequestStatus from, RequestStatus to) {
    rules.check(from, to);
    return to;
  }
}
