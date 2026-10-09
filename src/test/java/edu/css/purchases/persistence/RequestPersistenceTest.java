package edu.css.purchases.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.css.purchases.config.Application;
import edu.css.purchases.config.RequestService;
import edu.css.purchases.domain.DuplicateRequest;
import edu.css.purchases.domain.RequestId;
import edu.css.purchases.domain.RequestStatus;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

// NO @Transactional here: the count must be read from what was really committed.
@SpringBootTest(classes = Application.class)
class RequestPersistenceTest {

  @Autowired RequestService service;
  @Autowired RequestJdbc requests;
  @Autowired JdbcTemplate jdbc;

  @BeforeEach
  void cleanDatabase() throws IOException {
    jdbc.execute("DROP TABLE IF EXISTS purchase_request");
    jdbc.execute(Files.readString(Path.of("src/main/resources/db/schema.sql")));
  }

  @Test
  void secondStatementRollsBack() {
    assertThrows(DuplicateRequest.class, () -> service.insertTwice("PR-19"));
    assertEquals(0, requests.count("PR-19"));
  }

  @Test
  void secondRequestKeepsTheFirst() {
    service.register(RequestId.newId(), "PR-19", "Laptops");
    assertThrows(DuplicateRequest.class,
        () -> service.register(RequestId.newId(), "PR-19", "Again"));
    assertEquals(1, requests.count("PR-19"));
  }

  @Test
  void registeredRequestStartsAsDraft() {
    service.register(RequestId.newId(), "PR-20", "Monitors");
    assertEquals(Optional.of(RequestStatus.DRAFT), requests.findStatus("PR-20"));
  }
}
