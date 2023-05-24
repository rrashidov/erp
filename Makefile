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

## clean: clean the Java artifacts built
.PHONY: clean
clean:
	@echo "Start cleaning project"
	@${mvn_home} -T 1C clean
	@echo "Finished cleaning project"

## build: builds project modules
.PHONY: build
build:
	@echo "Start building erp components"
	@${mvn_home} -T 1C clean install
	@echo "Finished building erp components"

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

## run-erp-mysql: runs container with MySQL to use as DB
.PHONY: run-erp-mysql
run-erp-mysql:
	@echo "Make sure no leftover container is up and running"
	@docker stop erp-mysql || true
	@docker rm erp-mysql || true
	@echo "Leftover erp-mysql container cleaned, start a new one"
	@docker run -d -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=erp -e MYSQL_USER=erp -e MYSQL_PASSWORD=erp --name erp-mysql mysql:8.0.32
	@echo "erp-mysql container is up and running"

## build-erp-rabbitmq: builds container image based on RabbitMQ with custom config
.PHONY: build-erp-rabbitmq
build-erp-rabbitmq:
	@echo "Build RabbitMQ container image to be used by erp components"
	@docker build -t erp.rabbitmq:0.0.1 -f ./docker/Dockerfile.rabbitmq ./docker
	@echo "Finished building RabbitMQ container image to be used by erp components"

## locally: stops anything run locally
.PHONY: stop-locally
stop-locally:
	@echo "Stop locally running erp"
	@docker-compose -f ./docker/docker-compose.yml down
	@echo "Locally running erp stopped"

## start-locally: starts the whole system locally using docker compose
.PHONY: start-locally
start-locally: stop-locally containerize build-erp-rabbitmq
	@echo "Start erp setup locally"
	@docker-compose -f ./docker/docker-compose.yml up -d 
	@echo "erp is up and running locally. You can access it at http://localhost:8081"

## clean-local-mysql: cleans MySQL data
.PHONY: clean-local-mysql
clean-local-mysql:
	@echo "Start cleaning local mysql data"
	@docker-compose -f ./docker/docker-compose.yml down -v
	@echo "Finished cleaning local mysql data"

## init-test-data: initialises some test data
.PHONY: init-test-data
init-test-data:
	@echo "Start initializing test data"
	./scripts/init-test-data.sh
	@echo "Finished initializing test data"