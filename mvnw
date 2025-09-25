#!/usr/bin/env bash
# Minimal Maven wrapper stub for environments where mvnw isn't present.
# If you have a real project, commit the full Maven wrapper. This stub will try to use system mvn.

if command -v mvn >/dev/null 2>&1; then
  mvn "$@"
else
  echo "mvn is not installed. Please install Maven or add the Maven Wrapper to the repo."
  exit 1
fi
