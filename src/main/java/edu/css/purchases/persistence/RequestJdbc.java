package edu.css.purchases.persistence;

import edu.css.purchases.domain.RequestStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RequestJdbc {

  private final JdbcTemplate jdbc;

  public RequestJdbc(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public void insert(UUID id, String businessKey, String status, String title) {
    jdbc.update("""
        INSERT INTO purchase_request (id, business_key, status, title)
        VALUES (?, ?, ?, ?)
        """, id, businessKey, status, title);
  }

  public int count(String businessKey) {
    Integer n = jdbc.queryForObject(
        "SELECT count(*) FROM purchase_request WHERE business_key = ?",
        Integer.class,
        businessKey);
    return n == null ? 0 : n;
  }

  /** Text column -> enum. An unknown text throws IllegalArgumentException, it never becomes a new status. */
  public Optional<RequestStatus> findStatus(String businessKey) {
    return jdbc.query(
            "SELECT status FROM purchase_request WHERE business_key = ?",
            (rs, row) -> RequestStatus.valueOf(rs.getString("status")),
            businessKey)
        .stream()
        .findFirst();
  }
}
