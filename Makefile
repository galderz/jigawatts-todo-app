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

jar = target/quarkus-app/quarkus-run.jar
mvn += JAVA_HOME=/opt/java-17
mvn += /opt/maven/bin/mvn
sources += $(shell find ./ -type f -name '*.java' | sed 's: :\\ :g')
sources += $(shell find ./ -type f -name 'pom.xml' | sed 's: :\\ :g')
sources += $(shell find ./ -type f -name '*.html' | sed 's: :\\ :g')
sources += $(shell find ./ -type f -name '*.js' | sed 's: :\\ :g')

run: $(jar)
> sudo java -jar $<
.PHONY: run

$(jar): $(sources)
> $(mvn) package -DskipTests

checkpoint:
> curl -v -X POST http://localhost:8080/api/checkpoint
.PHONY: checkpoint

dev:
> $(mvn) -Dquarkus.http.host=0.0.0.0 quarkus:dev
.PHONY: dev

clean:
> sudo rm -drf target
.PHONY: clean
