package edu.css.purchases.persistence;

import edu.css.purchases.domain.RequestStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RequestJpa {

  @PersistenceContext
  private EntityManager em;

  public void insert(RequestEntity request) {
    em.persist(request);
  }

  public int count(String businessKey) {
    Long n = em.createQuery(
            "select count(r) from RequestEntity r where r.businessKey = :key", Long.class)
        .setParameter("key", businessKey)
        .getSingleResult();
    return n.intValue();
  }

  public Optional<RequestStatus> findStatus(String businessKey) {
    List<String> statuses = em.createQuery(
            "select r.status from RequestEntity r where r.businessKey = :key", String.class)
        .setParameter("key", businessKey)
        .getResultList();
    return statuses.stream().findFirst().map(RequestMapper::toStatus);
  }

  public void flush() {
    em.flush();
  }
}