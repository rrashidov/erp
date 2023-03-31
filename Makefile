# Include variables from the .envrc file
include .envrc

work_dir=$(pwd)

.PHONY: build
build:
	@echo "Start building erp components"
	@${mvn_home} clean install
	@echo "Finished building erp components"

.PHONY: containerize
containerize: build
	@echo "Start preparing docker context"
	@cp erp.backend/target/backend-0.0.1-SNAPSHOT.jar docker/backend.jar
	@cp erp.frontend/target/frontend-0.0.1-SNAPSHOT.jar docker/frontend.jar
	@echo "Finished preparing docker context"
	
	@echo "Start building erp container images"
	@docker build -t erp.frontend:0.0.1 -f ./docker/Dockerfile.frontend ./docker
	@docker build -t erp.backend:0.0.1 -f ./docker/Dockerfile.backend ./docker
	@echo "Finished building erp container images"

	@echo "Start cleaning up after container images are built"
	@rm ./docker/frontend.jar
	@rm ./docker/backend.jar
	@echo "Finished cleaning up after container images are built"

.PHONY: run-erp-mysql
run-erp-mysql:
	@echo "Make sure no leftover container is up and running"
	@docker stop erp-mysql || true
	@docker rm erp-mysql || true
	@echo "Leftover erp-mysql container cleaned, start a new one"
	@docker run -d -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=erp -e MYSQL_USER=erp -e MYSQL_PASSWORD=erp --name erp-mysql mysql:8.0.32
	@echo "erp-mysql container is up and running"

.PHONY: build-erp-rabbitmq
build-erp-rabbitmq:
	@echo "Build RabbitMQ container image to be used by erp components"
	@docker build -t erp.rabbitmq:0.0.1 -f ./docker/Dockerfile.rabbitmq ./docker
	@echo "Finished building RabbitMQ container image to be used by erp components"

.PHONY: start-locally
start-locally: containerize build-erp-rabbitmq
	@echo "Start erp setup locally"
	@docker-compose -f ./docker/docker-compose.yml up -d 
	@echo "erp is up and running locally. You can access it at http://localhost:8081"

.PHONY: stop-locally
stop-locally:
	@echo "Stop locally running erp"
	@docker-compose -f ./docker/docker-compose.yml down
	@echo "Locally running erp stopped"

.PHONY: test-locally
test-locally: stop-locally start-locally
	@echo "Go test it by hand"