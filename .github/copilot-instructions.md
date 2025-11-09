## Purpose
This project is a Spring Boot (Java 21) REST API for the kOzi application. The goal of this document is to give AI coding agents immediate, actionable context so they can be productive without asking basic repo questions.

## Quick facts
- Framework: Spring Boot 3.5.7 (see `pom.xml`)
- Java: 21
- Build: Maven (project includes `mvnw` and `mvnw.cmd`)
- API docs: springdoc OpenAPI (Swagger UI enabled)
- DB: PostgreSQL (runtime dependency in `pom.xml`)
- HATEOAS: project uses assembler classes (see `assembler/`)

## How to run (developer / CI)
- Windows PowerShell (repo root):
  - Run with dev profile: `.\\mvnw.cmd -Dspring.profiles.active=dev spring-boot:run`
  - Run tests: `.\\mvnw.cmd test`
  - Build artifact: `.\\mvnw.cmd clean package`
  - Build Docker image (option A - Spring Boot build-image): `.\\mvnw.cmd spring-boot:build-image`
  - OR use the provided `Dockerfile`: `docker build -t koziapi .`

Notes: profile-specific config files are in `src/main/resources/` (`application-dev.properties`, `application-prod.properties`, `application.properties`).

## Repo layout & key entry points
- `src/main/java/com/kozi/koziAPI/KoziApiApplication.java` — Spring Boot main class.
- `src/main/java/com/kozi/koziAPI/controller/` — REST controllers. There are both singular and plural controllers (e.g. `CategoriaController.java` and `CategoriasController.java`) — pay attention to which one is used for single-resource vs collection endpoints.
- `src/main/java/com/kozi/koziAPI/service/` — business logic layer. Controllers call services rather than talking to repositories directly in most places.
- `src/main/java/com/kozi/koziAPI/repository/` — Spring Data JPA repositories.
- `src/main/java/com/kozi/koziAPI/assembler/` — ModelAssembler classes that build HATEOAS models for responses. Typical pattern: `<Entity>ModelAssembler`.
- `src/main/java/com/kozi/koziAPI/config/` — configuration classes; `CorsConfig.java` customizes CORS. Security starter is present but dev setup keeps things permissive via project config.
- `src/main/resources/` — properties and static/templates used by the app.

## Important patterns & conventions (use these exactly)
- HATEOAS assemblers: controllers typically delegate to an assembler to produce response models. Look for `*ModelAssembler.java` in `assembler/` and mirror its usage in controllers.
- Naming & language: code uses Spanish domain names (`Producto`, `Pedido`, `Categoria`, etc.). Keep generated identifiers and messages consistent with existing names.
- Plural vs singular controllers: some controllers are named with plural resources (e.g. `CategoriasController`) for collection endpoints. Always inspect the controller file you modify to respect its routing/purpose.
- DTO / model conventions: model classes live under `model/` and are often mapped by assemblers — prefer reusing existing ModelAssembler patterns rather than returning raw entities.
- Lombok: the project uses Lombok. Ensure annotation processing is preserved (Maven setup already includes it).

## Integration points
- PostgreSQL — runtime DB. Connection is configured via `application-*.properties`. In dev you can run with an in-container Postgres or a local DB. Tests rely on Spring Boot testing support.
- Swagger UI — springdoc exposes API docs; check `/swagger-ui.html` or `/swagger-ui/index.html` when the app is running.
- Docker — there is a `Dockerfile` at repo root for container builds.

## Files to read first when implementing changes
- `pom.xml` — dependency & plugin versions (Java 21, Spring Boot parent)
- `HELP.md` — project-level guidance and references
- `src/main/java/com/kozi/koziAPI/config/CorsConfig.java` — CORS/security defaults for dev
- Example pair: `assembler/<X>ModelAssembler.java` + `controller/<X>Controller.java` — follow the same HATEOAS patterns.

## Examples (how to implement common tasks)
- Add a new GET endpoint that returns HATEOAS model:
  1. Create a JPA entity in `model/` and repository in `repository/`.
  2. Add service method in `service/` to fetch data.
  3. Add `<Entity>ModelAssembler` in `assembler/` (follow existing assemblers).
  4. Wire a route in `controller/` that calls the service and assembler.

## Quick debugging hints
- If things fail to start, check `src/main/resources/application-*.properties` for missing DB credentials.
- Lombok-generated methods: IDEs must have Lombok plugin enabled. Build-wise, Maven handles annotation processing.
- For CORS or 401 issues, check `config/CorsConfig.java` and any security config under `config/`.

## What not to change lightly
- Global packaging/dependency versions in `pom.xml` (Spring Boot parent and Java version).
- HATEOAS assembler interfaces — many controllers rely on consistent model shapes.

## When you need help
- If a pattern appears inconsistent (mixed use of assemblers or direct entity returns), add a note explaining observed behavior and propose a minimal, local change with rationale.

---
If any section is unclear or you'd like examples added (controller + assembler snippets for a specific resource), tell me which resource and I will add a short example.
