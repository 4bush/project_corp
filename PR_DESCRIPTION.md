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
- [x] No REST controller, no cloud passwords, no `target/` in git
