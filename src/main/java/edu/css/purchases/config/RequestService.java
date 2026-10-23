package edu.css.purchases.config;

import edu.css.purchases.domain.DuplicateRequest;
import edu.css.purchases.domain.RequestId;
import edu.css.purchases.domain.RequestStatus;
import edu.css.purchases.domain.Rule;
import edu.css.purchases.persistence.RequestJpa;
import edu.css.purchases.persistence.RequestMapper;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {

  private final Rule rules;
  private final RequestJpa requests;

  public RequestService(Rule rules, RequestJpa requests) {
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
      requests.insert(RequestMapper.toEntity(id.uuid(), businessKey, RequestStatus.DRAFT, title));
      requests.flush();
    } catch (DataIntegrityViolationException ex) {
      throw new DuplicateRequest(businessKey);
    }
  }

  /** One transaction, two INSERTs. The second fails -> rollback -> nothing is saved. */
  @Transactional
  public void insertTwice(String businessKey) {
    requests.insert(RequestMapper.toEntity(
        UUID.randomUUID(), businessKey, RequestStatus.DRAFT, "Laptops"));
    try {
      requests.insert(RequestMapper.toEntity(
          UUID.randomUUID(), businessKey, RequestStatus.DRAFT, "Laptops again"));
      requests.flush();
    } catch (DataIntegrityViolationException ex) {
      throw new DuplicateRequest(businessKey);
    }
  }
}
