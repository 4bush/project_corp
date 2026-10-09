package edu.css.purchases.domain;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** The status table: which move is allowed at all. */
public class TransitionRule implements Rule {

  private static final Map<RequestStatus, Set<RequestStatus>> ALLOWED = Map.of(
      RequestStatus.DRAFT, Set.of(RequestStatus.APPROVED),
      RequestStatus.APPROVED, Set.of(RequestStatus.ORDERED),
      RequestStatus.ORDERED, Set.of());

  @Override
  public void check(RequestStatus from, RequestStatus to) {
    Objects.requireNonNull(from, "from");
    Objects.requireNonNull(to, "to");
    if (!ALLOWED.get(from).contains(to)) {
      throw new IllegalStateException("move not allowed: " + from + " -> " + to);
    }
  }
}
