# Include variables from the .envrc file
include .envrc

.PHONY: build
build:
	@${mvn_home} clean install
