# AGENTS.md

## Quick start

```powershell
./mvnw.cmd spring-boot:run   # Run dev server
./mvnw.cmd test               # Run all tests
./mvnw.cmd package            # Build JAR (output in target/)
```

## Project structure

- **Spring Boot 4.0.6** / **Java 17** — single-module Maven project
- Main class: `com.example.day04.Day04Application`
- Package root: `com.example.day04`
- Config: `src/main/resources/application.properties`

## Conventions

- All new controllers/services go under `com.example.day04` or a sub-package
- Add `spring-boot-starter-webmvc-test` for integration tests (already configured)
- Use `@SpringBootTest` for integration tests (existing pattern)
- No controllers exist yet — this is a starter scaffold

## Commands

| Action | Command |
|---|---|
| Run app | `./mvnw.cmd spring-boot:run` |
| Run tests | `./mvnw.cmd test` |
| Run single test | `./mvnw.cmd test -Dtest=Day04ApplicationTests` |
| Build JAR | `./mvnw.cmd package -DskipTests` |
| Skip tests | append `-DskipTests` |
