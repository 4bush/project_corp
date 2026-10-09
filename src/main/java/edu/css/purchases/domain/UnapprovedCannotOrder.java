package edu.css.purchases.domain;

/** Stop-factor: money is never spent on a request nobody approved. */
public class UnapprovedCannotOrder implements Rule {

  @Override
  public void check(RequestStatus from, RequestStatus to) {
    if (to == RequestStatus.ORDERED && from != RequestStatus.APPROVED) {
      throw new IllegalStateException("cannot order a request that is not approved (was " + from + ")");
    }
  }
}
