package edu.css.purchases.domain;

import java.util.List;

/** Runs all rules one after another. The first one that objects wins. */
public class ChainRule implements Rule {

  private final List<Rule> rules;

  public ChainRule(List<Rule> rules) {
    this.rules = List.copyOf(rules);
  }

  @Override
  public void check(RequestStatus from, RequestStatus to) {
    for (Rule rule : rules) {
      rule.check(from, to);
    }
  }
}
