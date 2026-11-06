package edu.css.purchases.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    properties = "spring.sql.init.mode=never")
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ActuatorExposureTest {

  @Autowired MockMvc mvc;

  @Test
  void exposesOnlyHealthAndInfoActuatorEndpoints() throws Exception {
    mvc.perform(get("/actuator/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("UP"));

    mvc.perform(get("/actuator/env"))
        .andExpect(status().isNotFound());

    mvc.perform(get("/actuator/heapdump"))
        .andExpect(status().isNotFound());
  }
}
