package edu.css.purchases.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = "spring.sql.init.mode=never")
@ActiveProfiles("dev")
class DownstreamPropertiesTest {

  @Autowired DownstreamProperties properties;

  @Test
  void bindsDevDownstreamSettingsWithoutMakingNetworkCalls() {
    assertEquals("http://localhost:9090", properties.baseUrl());
    assertEquals(Duration.ofSeconds(2), properties.timeout());
  }
}
