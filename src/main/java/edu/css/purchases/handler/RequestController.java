package edu.css.purchases.handler;

import edu.css.purchases.config.RequestService;
import edu.css.purchases.domain.PurchaseRequest;
import edu.css.purchases.domain.RequestId;
import edu.css.purchases.domain.RequestStatus;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/purchase-requests")
public class RequestController {

  private final RequestService service;

  public RequestController(RequestService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<PurchaseRequestResponse> create(
      @Valid @RequestBody PurchaseRequestRequest request) {
    RequestId id = RequestId.newId();
    service.register(id, request.businessKey(), request.title());
    PurchaseRequestResponse response = new PurchaseRequestResponse(
        id.value(), request.businessKey(), RequestStatus.DRAFT, request.title());
    return ResponseEntity.created(URI.create("/api/v1/purchase-requests/" + id.value()))
        .body(response);
  }

  @GetMapping("/{id}")
  public PurchaseRequestResponse getById(@PathVariable String id) {
    RequestId requestId = new RequestId(id);
    requestId.uuid();
    PurchaseRequest request = service.findById(requestId);
    return new PurchaseRequestResponse(
        request.id().value(), request.businessKey(), request.status(), request.title());
  }
}
