package edu.css.purchases.config;

import edu.css.purchases.domain.DuplicateRequest;
import edu.css.purchases.domain.RequestId;
import edu.css.purchases.domain.RequestStatus;
import edu.css.purchases.domain.Rule;
import edu.css.purchases.persistence.RequestJdbc;
import java.util.UUID;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {

  private final Rule rules;
  private final RequestJdbc requests;

  public RequestService(Rule rules, RequestJdbc requests) {
    this.rules = rules;
    this.requests = requests;
  }

  public RequestStatus move(RequestStatus from, RequestStatus to) {
    rules.check(from, to);
    return to;
  }

  /** One transaction per call. The first call commits, the second one with the same key throws. */
  @Transactional
  public void register(RequestId id, String businessKey, String title) {
    if (businessKey == null || businessKey.isBlank()) {
      throw new IllegalArgumentException("business key must not be blank");
    }
    try {
      requests.insert(id.uuid(), businessKey, RequestStatus.DRAFT.name(), title);
    } catch (DuplicateKeyException ex) { // SQLState 23505 (subclass of DataIntegrityViolationException)
      throw new DuplicateRequest(businessKey);
    }
  }

  /** One transaction, two INSERTs. The second fails -> rollback -> nothing is saved. */
  @Transactional
  public void insertTwice(String businessKey) {
    requests.insert(UUID.randomUUID(), businessKey, RequestStatus.DRAFT.name(), "Laptops");
    try {
      requests.insert(UUID.randomUUID(), businessKey, RequestStatus.DRAFT.name(), "Laptops again");
    } catch (DuplicateKeyException ex) {
      throw new DuplicateRequest(businessKey); // unchecked -> rollback
    }
  }
}
