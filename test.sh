#!/bin/bash

mvn clean install
cd target
mvn archetype:generate \
        -DarchetypeGroupId=com.github.dilbertside \
        -DarchetypeArtifactId=spring-mvc-start \
        -DarchetypeVersion=5.1.0 \
        -DgroupId=com.test.test \
        -Dpackage=com.test.test \
        -DartifactId=test \
        -Dversion=0.0.1
cd test
mvn test -Ptest
        
