# Include variables from the .envrc file
include .envrc

work_dir=$(pwd)

# Per-run host ports, picked by scripts/pick-free-ports.sh so local runs don't
# collide with ports already taken by other processes/containers on the host.
dev_env_files=--env-file ./docker/env.dev --env-file ./docker/env.dev.ports

# ==================================================================================== #
# HELPERS
# ==================================================================================== #

## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

# ==================================================================================== #
# BUILD
# ==================================================================================== #

## build: builds project modules
.PHONY: build
build:
	@echo "Start building erp components"
	@${mvn_home} -T 1C clean install
	@echo "Finished building erp components"

## clean: clean the Java artifacts built
.PHONY: clean
clean:
	@echo "Start cleaning project"
	@${mvn_home} -T 1C clean
	@echo "Finished cleaning project"

# ==================================================================================== #
# CONTAINER IMAGES
# ==================================================================================== #

## containerize: builds container images with Java components
.PHONY: containerize
containerize: build
	@echo "Start preparing docker context"
	@cp erp.backend/target/backend-0.0.1-SNAPSHOT.jar docker/backend.jar
	@cp erp.frontend/target/frontend-0.0.1-SNAPSHOT.jar docker/frontend.jar
	@echo "Finished preparing docker context"
	
	@echo "Start building erp container images"
	@docker build -t erp.commonjava:0.0.1 -f ./docker/Dockerfile.commonjava ./docker
	@docker build -t erp.frontend:0.0.1 -f ./docker/Dockerfile.frontend ./docker
	@docker build -t erp.backend:0.0.1 -f ./docker/Dockerfile.backend ./docker
	@echo "Finished building erp container images"

	@echo "Start cleaning up after container images are built"
	@rm ./docker/frontend.jar
	@rm ./docker/backend.jar
	@echo "Finished cleaning up after container images are built"

## build-erp-rabbitmq: builds container image based on RabbitMQ with custom config
.PHONY: build-erp-rabbitmq
build-erp-rabbitmq:
	@echo "Build RabbitMQ container image to be used by erp components"
	@docker build -t erp.rabbitmq:0.0.1 -f ./docker/Dockerfile.rabbitmq ./docker
	@echo "Finished building RabbitMQ container image to be used by erp components"

## build-erp-mysqlbackup: builds container image to backup MySQL DB
.PHONY: build-erp-mysqlbackup
build-erp-mysqlbackup:
	@echo "Build mysqlbackup container image to be used by erp components"
	@docker build -t erp.mysqlbackup:0.0.1 -f ./docker/Dockerfile.mysqlbackup ./docker
	@echo "Finished building mysqlbackup container image to be used by erp components"

# ==================================================================================== #
# DATA
# ==================================================================================== #

## init-test-data: initialises some test data
.PHONY: init-test-data
init-test-data: ensure-dev-ports
	@echo "Start initializing test data"
	@. ./docker/env.dev.ports; BASE_URL="localhost:$${BACKEND_PORT:-8082}" ./scripts/init-test-data.sh
	@echo "Finished initializing test data"

## clean-local-mysql: cleans MySQL data
.PHONY: clean-local-mysql
clean-local-mysql: ensure-dev-ports
	@echo "Start cleaning local mysql data"
	@docker compose -f ./docker/docker-compose-dev.yml --project-name dev $(dev_env_files) down -v
	@echo "Finished cleaning local mysql data"

# ==================================================================================== #
# LOCAL SETUP
# ==================================================================================== #

## ensure-dev-ports: makes sure a docker/env.dev.ports file exists (created on demand with fallback defaults)
.PHONY: ensure-dev-ports
ensure-dev-ports:
	@test -f ./docker/env.dev.ports || printf 'BACKEND_PORT=8082\nFRONTEND_PORT=8081\nRABBITMQ_ADMIN_PORT=15672\n' > ./docker/env.dev.ports

## pick-free-ports: finds free host ports for backend/frontend/rabbitmq-admin and writes docker/env.dev.ports
.PHONY: pick-free-ports
pick-free-ports:
	@./scripts/pick-free-ports.sh

## start-locally: starts the whole system locally using docker compose
.PHONY: start-locally
start-locally: stop-locally pick-free-ports containerize build-erp-rabbitmq build-erp-mysqlbackup
	@echo "Start erp setup locally"
	@docker compose -f ./docker/docker-compose-dev.yml --project-name dev $(dev_env_files) up -d
	@. ./docker/env.dev.ports; echo "erp is up and running locally. You can access it at http://localhost:$${FRONTEND_PORT:-8081}"

## stop-locally: stops anything run locally
.PHONY: stop-locally
stop-locally: ensure-dev-ports
	@echo "Stop locally running erp"
	@docker compose -f ./docker/docker-compose-dev.yml --project-name dev $(dev_env_files) down
	@echo "Locally running erp stopped"

# ==================================================================================== #
# Testing
# ==================================================================================== #

## run-integration-tests: runs integration tests towards backend API
.PHONY: run-integration-tests
run-integration-tests: stop-locally clean-local-mysql start-locally
	@echo "Start running integration tests"
	@. ./docker/env.dev.ports; ERP_BACKENDURL="http://localhost:$${BACKEND_PORT:-8082}" java -jar ./itests/target/itests-0.0.1-SNAPSHOT.jar
	@echo "Finished running integration tests. Check their output to see if the pass"

## verify: builds, starts stack, runs integration tests; leaves the stack up on failure for investigation (use `make verify-cleanup` afterwards)
.PHONY: verify
verify: stop-locally clean-local-mysql start-locally
	@echo "Start running integration tests"
	@. ./docker/env.dev.ports; ERP_BACKENDURL="http://localhost:$${BACKEND_PORT:-8082}" java -jar ./itests/target/itests-0.0.1-SNAPSHOT.jar; \
	EXIT_CODE=$$?; \
	if [ $$EXIT_CODE -eq 0 ]; then \
		make stop-locally; \
	else \
		echo "Integration tests failed - leaving the local stack running for investigation. Run 'make verify-cleanup' when done."; \
	fi; \
	exit $$EXIT_CODE

## verify-cleanup: stops the stack left running by a failed `make verify`
.PHONY: verify-cleanup
verify-cleanup: stop-locally

# ==================================================================================== #
# Production
# ==================================================================================== #

## start-production: creates and starts production docker compose instance
.PHONY: start-production
start-production: stop-production containerize build-erp-rabbitmq build-erp-mysqlbackup
	@echo "Starting production docker compose instance"
	@docker compose -f ./docker/docker-compose-prod.yml --project-name prod --env-file ./docker/env.prod up -d
	@echo "Finished starting production docker compose instance"

## stop-production: stops production docker compose instance
stop-production:
	@echo "Stopping production docker compose instance"
	@docker compose -f ./docker/docker-compose-prod.yml --project-name prod --env-file ./docker/env.prod down
	@echo "Finished stopping production docker compose instance"
