#!/bin/bash

# check dependencies
(
    type mvn &>/dev/null || ( echo "maven is not available"; exit 1 )
)>&2

VERSION_SMSA=5.1.2
VERSION_BOM=5.1.0
mvn -q versions:set -DnewVersion="$VERSION_SMSA"
#mvn versions:update-parent "-DparentVersion=$VERSION_BOM"
#mvn -q versions:set-property -Dproperty=spring-mvc-start-archetype.version -DnewVersion="$VERSION"
mvn clean install
cd target
mvn -B archetype:generate \
        -DarchetypeGroupId=com.github.dilbertside \
        -DarchetypeArtifactId=spring-mvc-start-archetype \
        -DarchetypeVersion=$VERSION_SMSA
cd webapp
mvn test -Ptest
        
