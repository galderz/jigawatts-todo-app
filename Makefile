SHELL := bash
.ONESHELL:
.SHELLFLAGS := -eu -o pipefail -c
.DELETE_ON_ERROR:
MAKEFLAGS += --warn-undefined-variables
MAKEFLAGS += --no-builtin-rules

ifeq ($(origin .RECIPEPREFIX), undefined)
  $(error This Make does not support .RECIPEPREFIX. Please use GNU Make 4.0 or later)
endif
.RECIPEPREFIX = >

mvn += JAVA_HOME=/opt/java-17
mvn += /opt/maven/bin/mvn

dev:
> $(mvn) -Dquarkus.http.host=0.0.0.0 quarkus:dev
.PHONY: dev
