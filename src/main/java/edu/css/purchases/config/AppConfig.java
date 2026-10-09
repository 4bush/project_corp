package edu.css.purchases.config;

import edu.css.purchases.domain.ChainRule;
import edu.css.purchases.domain.RequestPolicy;
import edu.css.purchases.domain.Rule;
import edu.css.purchases.domain.TransitionRule;
import edu.css.purchases.domain.UnapprovedCannotOrder;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** The only place that wires plain domain classes into Spring. */
@Configuration
public class AppConfig {

  @Bean
  Rule requestRules() {
    return new ChainRule(List.of(new UnapprovedCannotOrder(), new TransitionRule()));
  }

  @Bean
  RequestPolicy requestPolicy(Rule requestRules) {
    return new RequestPolicy(requestRules);
  }
}
