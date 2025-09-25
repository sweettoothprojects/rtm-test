# Spring Boot Sample (Buildpacks)

This is a minimal Spring Boot sample application (Java 17) configured to build an OCI image using Cloud Native Buildpacks (Paketo) via the `spring-boot-maven-plugin`.

Quick commands:

Build and run locally:

```bash
./mvnw -B -DskipTests package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

Build OCI image with buildpacks (no Docker daemon required):

```bash
./mvnw -B -DskipTests spring-boot:build-image
```

Run image locally (requires Docker):

```bash
docker run --rm -p 8080:8080 ghcr.io/com.example/demo:0.0.1-SNAPSHOT
```

Health check:

- GET http://localhost:8080/greet

Notes:

- The `pom.xml` sets the builder to `paketobuildpacks/builder:base`. Change it if you prefer another builder.
- Image name defaults to `ghcr.io/com.example/demo:0.0.1-SNAPSHOT` — change `pom.xml` or set `-Dspring-boot.build-image.imageName=...` when invoking the goal.
