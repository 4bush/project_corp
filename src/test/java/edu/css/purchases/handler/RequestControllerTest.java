package edu.css.purchases.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.css.purchases.config.RequestService;
import edu.css.purchases.config.Application;
import edu.css.purchases.domain.PurchaseRequest;
import edu.css.purchases.domain.RequestId;
import edu.css.purchases.domain.RequestNotFoundException;
import edu.css.purchases.domain.RequestStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(RequestController.class)
@ContextConfiguration(classes = Application.class)
@Import(RequestExceptionHandler.class)
class RequestControllerTest {

  private static final String BASE_PATH = "/api/v1/purchase-requests";

  @Autowired MockMvc mvc;
  @MockitoBean RequestService service;

  @Test
  void createsRequestAndReturnsCreatedResponse() throws Exception {
    mvc.perform(post(BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"businessKey":"PR-21","title":"Laptops"}
                """))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location",
            org.hamcrest.Matchers.matchesRegex(BASE_PATH + "/[0-9a-f-]{36}")))
        .andExpect(jsonPath("$.businessKey").value("PR-21"))
        .andExpect(jsonPath("$.status").value("DRAFT"))
        .andExpect(jsonPath("$.title").value("Laptops"));

    verify(service).register(any(RequestId.class), eq("PR-21"), eq("Laptops"));
  }

  @Test
  void getsRequestById() throws Exception {
    RequestId id = new RequestId("9b67f04e-c9f6-4f4c-9fb5-b7c2d59fbc51");
    when(service.findById(id)).thenReturn(
        new PurchaseRequest(id, "PR-22", RequestStatus.APPROVED, "Monitors"));

    mvc.perform(get(BASE_PATH + "/" + id.value()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.value()))
        .andExpect(jsonPath("$.businessKey").value("PR-22"))
        .andExpect(jsonPath("$.status").value("APPROVED"))
        .andExpect(jsonPath("$.title").value("Monitors"));
  }

  @Test
  void returnsNotFoundForUnknownRequest() throws Exception {
    RequestId id = new RequestId("9b67f04e-c9f6-4f4c-9fb5-b7c2d59fbc51");
    when(service.findById(id)).thenThrow(new RequestNotFoundException(id.value()));

    mvc.perform(get(BASE_PATH + "/" + id.value()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.path").value(BASE_PATH + "/" + id.value()));
  }

  @Test
  void rejectsBlankRequiredFields() throws Exception {
    mvc.perform(post(BASE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"businessKey":" ","title":""}
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").isNotEmpty());
  }

  @Test
  void rejectsMalformedRequestId() throws Exception {
    mvc.perform(get(BASE_PATH + "/not-a-uuid"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400));
  }
}
