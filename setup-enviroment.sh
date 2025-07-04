#!/bin/bash

# --- Configuration ---
# Exit immediately if a command exits with a non-zero status.
set -e

# Variables for easy configuration
COMPOSE_FILE="docker-compose-localstack.yml"
# Corrected URL definitions for clarity and correctness
LOCALSTACK_BASE_URL="http://localhost:4566"
HEALTH_URL="${LOCALSTACK_BASE_URL}/_localstack/health"
MAX_WAIT_SECONDS=120 # 2 minutes
WAIT_INTERVAL_SECONDS=2

# --- Main Script ---

# Function to check for required tools
check_dependencies() {
  echo "Checking for required tools..."
  if ! command -v docker &> /dev/null; then
    echo "Error: docker is not installed or not in your PATH."
    exit 1
  fi
  if ! command -v tflocal &> /dev/null; then
    echo "Error: tflocal (terraform-local) is not installed. Please install it."
    exit 1
  fi
  # The jq check has been removed.
  echo "All dependencies are present."
}

# Function to wait for LocalStack to be ready
wait_for_localstack() {
  echo "Waiting for LocalStack to be ready at ${HEALTH_URL}..."

  # Calculate the number of retries
  local retries=$((MAX_WAIT_SECONDS / WAIT_INTERVAL_SECONDS))

  for ((i=0; i<retries; i++)); do
    # MODIFIED: Check if S3 is in an "available" OR "running" state.
    # This makes the script more robust for both initial and subsequent runs.
    if curl -s "$HEALTH_URL" | grep -q -E '"s3": "(available|running)"'; then
      echo "LocalStack is ready."
      return 0
    fi
    sleep "$WAIT_INTERVAL_SECONDS"
  done

  echo "Error: LocalStack did not become ready within ${MAX_WAIT_SECONDS} seconds."
  exit 1
}

# --- Execution ---

check_dependencies

echo "Starting LocalStack using ${COMPOSE_FILE}..."
docker compose -f "$COMPOSE_FILE" up -d

wait_for_localstack

echo "Creating LocalStack resources with Terraform..."

cd terraform || { echo "Error: terraform/localstack directory not found."; exit 1; }
# Initialize Terraform Local
tflocal init

# Apply Terraform configuration with auto-approve
tflocal apply -auto-approve

echo "LocalStack resources created successfully."