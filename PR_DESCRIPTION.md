Title: CSS-18 join Spring Boot, CSS-32 schema and JDBC   (replace with your Jira IDs)

- [x] Story IDs in the title
- [x] Same product as Labs 1 and 2
- [x] `domain` has no `org.springframework` / `java.sql` import
- [x] Two types implement `Rule` (TransitionRule, UnapprovedCannotOrder)
- [x] Week 4 tests still green (status table + one mocked port)
- [x] `CREATE TABLE` has surrogate id, UNIQUE business key, CHECK on status
- [x] `secondStatementRollsBack` -> count is 0
- [x] `secondRequestKeepsTheFirst` -> count is 1
- [x] `DuplicateRequest` is unchecked and lives in `domain`
- [x] `schema.sql` applied on a clean database; repository uses JDBC and `?`
- [x] `mvn -q verify` is green
- [x] `mvn spring-boot:run` starts
- [x] No cloud passwords; `target/` is not tracked

## Lab 4 submission

### Week 9 — REST contract
- [x] POST creates a purchase request and returns `201 Created` with its location.
- [x] GET retrieves a purchase request by id.
- [x] Request validation and not-found responses are covered by WebMvc tests.
- [x] Duplicate business keys are mapped to HTTP 409; persistence test confirms the first row remains and the count is 1.
- [ ] Verify duplicate POST response in Postman.
- [ ] Capture Swagger screenshots.

### Week 10 — profiles, configuration, and Actuator
- [x] Default profile is `dev`; `application-dev.yaml` configures the local CSS database and downstream settings.
- [x] `application-prod.yaml` obtains database credentials from environment variables, including `${CSS_DB_PASSWORD}` only.
- [x] Downstream settings bind to `baseUrl` and `Duration timeout`; the Spring Boot test checks the dev values without a network call.
- [x] Actuator test confirms health is `200 UP`, while `/actuator/env` and `/actuator/heapdump` are `404`.
- [x] Main Java sources contain no `http://` or `jdbc:postgresql` string literals; domain has no Spring, `ResponseEntity`, or `jakarta.validation` references.
- [x] `application.yaml` disables error detail inclusion and exposes only health and info; `.gitignore` excludes `target/` and `*.log`.
- [x] `mvn -q clean verify` completed successfully.
- [ ] Check `/actuator/health` on the running application.

Второй POST того же business key возвращает 409 DUPLICATE; количество строк
для этого ключа остаётся 1.
