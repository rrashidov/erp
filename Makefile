# Include variables from the .envrc file
include .envrc

work_dir=$(pwd)

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
init-test-data:
	@echo "Start initializing test data"
	./scripts/init-test-data.sh
	@echo "Finished initializing test data"

## clean-local-mysql: cleans MySQL data
.PHONY: clean-local-mysql
clean-local-mysql:
	@echo "Start cleaning local mysql data"
	@docker compose -f ./docker/docker-compose-dev.yml --project-name dev --env-file ./docker/env.dev down -v
	@echo "Finished cleaning local mysql data"

# ==================================================================================== #
# LOCAL SETUP
# ==================================================================================== #

## start-locally: starts the whole system locally using docker compose
.PHONY: start-locally
start-locally: stop-locally containerize build-erp-rabbitmq build-erp-mysqlbackup
	@echo "Start erp setup locally"
	@docker compose -f ./docker/docker-compose-dev.yml --project-name dev --env-file ./docker/env.dev up -d 
	@echo "erp is up and running locally. You can access it at http://localhost:8081"

## stop-locally: stops anything run locally
.PHONY: stop-locally
stop-locally:
	@echo "Stop locally running erp"
	@docker compose -f ./docker/docker-compose-dev.yml --project-name dev --env-file ./docker/env.dev down
	@echo "Locally running erp stopped"

# ==================================================================================== #
# Testing 
# ==================================================================================== #

## run-integration-tests: runs integration tests towards backend API
.PHONY: run-integration-tests
run-integration-tests: stop-locally clean-local-mysql start-locally
	@echo "Start running integration tests"
	@java -jar ./itests/target/itests-0.0.1-SNAPSHOT.jar
	@echo "Finished running integration tests. Check their output to see if the pass"

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
