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