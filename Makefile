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

btm_jar = /opt/byteman/byteman/target/byteman-4.0.18.jar
btm_print = echo '$(1)'
btm_comma := ,
btm_script = script:src/main/resources/jigawatts.btm$(btm_comma)listener:true

jar := target/quarkus-app/quarkus-run.jar
jvm_opts += -javaagent:$(btm_jar)=$(btm_script)
jvm_opts += -XX:+UseSerialGC
jvm_opts += -XX:-UsePerfData
mvn += JAVA_HOME=/opt/java-17
mvn += /opt/maven/bin/mvn
sources += $(shell find ./ -type f -name '*.java' | sed 's: :\\ :g')
sources += $(shell find ./ -type f -name 'pom.xml' | sed 's: :\\ :g')
sources += $(shell find ./ -type f -name '*.properties' | sed 's: :\\ :g')
sources += $(shell find ./ -type f -name '*.btm' | sed 's: :\\ :g')

ifdef DEBUG
  jvm_opts += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:4004
endif

ifdef VERBOSE
  jvm_opts += -Dorg.jboss.byteman.verbose=true
endif

run: $(jar)
#> sudo java -jar $<
# Workaround hsperfdata_root error https://gist.github.com/galderz/46c17b4a87210496df2b8824789420cc
#> sudo java -XX:-UsePerfData -jar $<
> sudo java $(jvm_opts) -jar $<
.PHONY: run

run2: $(jar)
> sudo java -Dquarkus.http.port=8081 -jar $<
.PHONY: run2

$(jar): $(sources)
> $(mvn) package -DskipTests

checkpoint:
> curl -v -X POST http://localhost:8080/api/checkpoint
.PHONY: checkpoint

restore:
> curl -v -X POST http://localhost:8081/api/restore
.PHONY: restore

dev:
> $(mvn) -Dquarkus.http.host=0.0.0.0 quarkus:dev
.PHONY: dev

clean:
> sudo rm -drf target
.PHONY: clean
