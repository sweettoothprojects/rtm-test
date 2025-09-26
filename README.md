# Spring Boot Sample (Buildpacks)

This is a minimal Spring Boot sample application (Java 17) configured to build an OCI image using Cloud Native Buildpacks (Paketo) via the `spring-boot-maven-plugin`.

Quick commands

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
# example pulled image from ECR
docker run --rm -p 8080:8080 <aws_account>.dkr.ecr.<region>.amazonaws.com/<repo>:<tag>
```

Health check

- GET http://localhost:8080/greet

CI and image publishing

- This repository contains two GitHub Actions workflows:
	- `.github/workflows/ci.yml` — builds the project, constructs an OCI image (paketo buildpacks), tags the image with multiple tags (project version, incremented append version, and commit SHA) and pushes to Amazon ECR. It also optionally tags `latest` for `main`.
	- `.github/workflows/octopus-deploy.yml` — validates the pushed image, resolves the Octopus project by slug (if necessary), and creates an Octopus release via the Octopus API.

Required repository secrets and variables (set in GitHub Settings)

- Secrets (Repository > Settings > Secrets):
	- `ECR_REPOSITORY` — ECR repo name (e.g. `rtm-test`)
	- `AWS_REGION` — AWS region (e.g. `eu-west-1`)
	- `ECR_ACCOUNT_ID` — AWS account id for the ECR registry
	- `AWS_ROLE_TO_ASSUME` — the IAM role ARN the workflow will assume via OIDC (e.g. `arn:aws:iam::123456789012:role/GitHubActionsRole`)
	- `OCTOPUS_API_KEY` — Octopus API key with permission to create releases
	- `OCTOPUS_SERVER` — Octopus server base URL (e.g. `https://youroctopus.octopus.app`)

- Optional secrets:
	- `REPO_DOCKER_IMAGE` — local image name built by Maven (artifactId:version). If omitted the workflows compute a local image name from the Maven artifactId and version.
	- `REPO_IMAGE_TAG` — override image tag used by Octopus workflow when resolving/pulling images.
	- `OCTOPUS_PROJECT_ID` — numeric project id may be supplied but is optional; the Octopus workflow will resolve the project by slug if this is absent.

- Repository Variables (Repository > Settings > Variables):
	- `OCTOPUS_PROJECT_SLUG` — the octopus project slug (defaults to `rtm-test` in the workflow if not set).

How CI tags are produced

- The CI job tags the same image with these tags (by design):
	- Maven `project.version` (e.g. `0.0.1-SNAPSHOT` or `0.0.2`)
	- `appendVersion` — incremented patch/last segment derived from the semver
	- commit SHA (immutable, exact reference)
	- `latest` (only pushed when building on `main`)

Troubleshooting AWS OIDC assume-role

- The workflows use GitHub OIDC to assume an AWS IAM role. For this to succeed:
	- Add the GitHub OIDC Provider to your AWS account and configure the IAM role trust policy to allow your repository or organization to assume it.
	- Ensure the repository workflow has `permissions: id-token: write` (set in each workflow).
	- `AWS_ROLE_TO_ASSUME` must be the full role ARN.

Helpful ECR debugging commands

```bash
# list tags in a repo
aws ecr list-images --repository-name <repo> --region <region> --query 'imageIds[*].imageTag' --output text

# get image digest for a tag
aws ecr batch-get-image --repository-name <repo> --image-ids imageTag=<tag> --region <region> --query 'images[0].imageId.imageDigest' --output text
```

Octopus notes

- The Octopus workflow creates a release by POSTing to `/api/releases`. The workflow injects release variables: `ImageTag`, `ImageName`, `Registry`, `ImageDigest`, `Commit`, `Branch`, and `BuildUrl`.
- The workflow resolves the Octopus project id from `OCTOPUS_PROJECT_SLUG` if `OCTOPUS_PROJECT_ID` secret is not provided.

If you'd like me to add more docs (e.g., sample IAM trust policy for the role, or example Octopus step configuration that consumes the injected variables), tell me which one and I will add it.
