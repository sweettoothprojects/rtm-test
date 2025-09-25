<!-- Project-specific Copilot instructions for the Spring Boot buildpacks sample app -->
# Copilot instructions for this workspace

- Project type: Maven + Spring Boot (Java 17)
- Purpose: Minimal demo app that demonstrates building an OCI image with Paketo buildpacks via `spring-boot-maven-plugin`.

What to do first

- Use `./mvnw -B -DskipTests package` to compile and create the runnable JAR.
- Use `./mvnw -B -DskipTests spring-boot:build-image` to produce an OCI image with Cloud Native Buildpacks (Paketo).

Important files to inspect

- `pom.xml` — build configuration and `spring-boot-maven-plugin` image settings (builder + image name).
- `src/main/java/com/example/demo/DemoApplication.java` — application entry point.
- `src/main/java/com/example/demo/GreetingController.java` — example REST endpoint (GET /greet).
- `README.md` — quick commands for building and running locally.

Patterns and conventions

- Java 17 is used across the project (see `<java.version>` in `pom.xml`).
- The image builder is set to `paketobuildpacks/builder:base`; use `BP_JVM_VERSION` env for JVM selection.
- Image name uses `ghcr.io/${project.groupId}/${project.artifactId}:${project.version}` by default. Override with `-Dspring-boot.build-image.imageName=...`.

Example intents

- "Build the OCI image and start the container": run `./mvnw -DskipTests spring-boot:build-image` then `docker run -p 8080:8080 <image>`.
- "Change JVM version for the buildpack": edit `pom.xml` image env or pass `-DBP_JVM_VERSION=11.*` at build time.

Testing and CI

- Unit tests run with `./mvnw test`. The sample contains a basic integration test in `src/test/java`.

Limits

- Do not attempt to push images to registries without explicit credentials; the sample uses GHCR name as default only for examples.
- Do not change Java version below 17 unless you update `pom.xml` and plugin configuration.
