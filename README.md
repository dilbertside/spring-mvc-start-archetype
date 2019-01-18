Spring Web MVC 5.1 start Maven Archetype
======================================

# Status

[![Release](https://jitpack.io/v/dilbertside/spring-mvc-start-archetype.svg)](https://jitpack.io/#dilbertside/spring-mvc-start-archetype)
[![Build Status](https://travis-ci.org/dilbertside/spring-mvc-start-archetype.svg)](https://travis-ci.org/dilbertside/spring-mvc-start-archetype)

# Summary

The project is a Maven archetype for Spring MVC web application.
It is heavily inspired from https://github.io/kolorobot/spring-mvc-quickstart-archetype and was originally a fork.
kolorobot archetype is based onto Spring Framework 5.0.x (as of January 2019)

After refactoring a bit to upgrade Spring, the aim became to build a full Spring Framework MVC  Web application only (which should not contain any Spring Boot dependencies). In that purpose some extra configuration have been added Cache, Async, WebJars, Problem
Why?
While Spring Boot is a wonderful way to build a web application in no time, this archetype startup is a way to claim back control to all the opinionated choices made by Spring Boot team.
In that way, the configuration is not hidden in Auto Configuration properties. Saying that reading Boot code helps to build a configuration without Spring Boot. For a complete Spring Boot Application you may want to have a look to [JHipster](https://www.jhipster.tech/)

This starter can be used for 
* educational purpose
* migration purpose (ie from Spring Framework core 4.3.x to 5.1.x)
* as a starting point to migrate from Spring Boot to Spring Framework core and Web MVC.

Notes:
This archetype may not work with prior versions of Spring Framework 5.0.x, or 4.3.x, and Java 1.8
It uses methods which are not reverse compatible.

When Spring Framework 5.2 will be out a new branch to track progress will be created.

PR contributions are welcome as long as there is no Spring Boot dependency included.

## Generated project characteristics
* Bill of Material
* Spring MVC web application, with Java config and no Spring Boot
* [Thymeleaf](http://www.thymeleaf.org/), [Bootstrap](https://getbootstrap.com/) with [WebJars](http://webjars.org)
* Java Persistence (JPA)
** Spring Data JPA](https://spring.io/projects/spring-data)
** Hibernate](http://hibernate.org/orm)
** HSQLDB](http://hsqldb.org/)
** H2](http://www.h2database.com/)
** Postgresql](https://www.postgresql.org/)
* Testing with [JUnit]( http://junit.org )/[Mockito](https://github.com/mockito/mockito)
* [Spring Security](https://spring.io/projects/spring-security) 
* [MongoDB](http://www.mongodb.org/) (Spring Data Mongo)
* Docker containerization with [Google Jib maven plugin](https://github.com/GoogleContainerTools/jib)
* Cache memory with [Caffeine](https://github.com/ben-manes/caffeine)
* Email: Google mail ready
* [Jackson JSON/XML/CSV data-binding functionality](http://github.com/FasterXML/jackson)
* [Problem, implementation of the application/problem+json draft](https://github.com/zalando/problem)
* [Lombok](https://projectlombok.org/) to reduce boilerplate code (cf readme of generated project)

## TODO
  Localization improvements
  View.JS integration

## Prerequisites

- JDK 8 (tested with Java 11)
- [Apache Maven](https://maven.apache.org/download.cgi) 3 (tested with 3.6.0)
- not mandatory [Docker](https://docs.docker.com/install/#supported-platforms) 

# Create a new project

## command line
Java and maven must be installed.

```bash
    mvn archetype:generate \
        -DarchetypeGroupId=com.github.dilbertside \
        -DarchetypeArtifactId=spring-mvc-start \
        -DarchetypeVersion=5.1.0 \
        -DgroupId=my.groupid \
        -DartifactId=my-artifactId \
        -Dversion=version \
        -DarchetypeRepository=http://dilbertside.github.io/spring-mvc-start-archetype
```

Note: The above command will bootstrap a project using the archetype published here: http://dilbertside.github.io/spring-mvc-start-archetype


## Docker maven

Reusing the Maven local repository and in Generating project in Interactive mode

```bash
mkdir myproject && cd myproject

docker run -it --rm -v "$(pwd)":/usr/src/mymaven diside/spring-mvc-start-archetype-docker:5.1.0 mvn archetype:generate \
      -DarchetypeGroupId=com.github.dilbertside \
      -DarchetypeArtifactId=spring-mvc-start \
      -DarchetypeVersion=5.1.0
```

## IntelliJ

* Create new project `File > New > Project`
* Click Maven on the left hand side of the new project dialog
* Check `Create from archetype`
* Click the `Add Archetype` button
* Set `Group Id` to `com.github.dilbertside`
* Set `Artifact Id` to `spring-mvc-start`
* Set `Version` to `5.1.0`
* Set `Repository` to `http://dilbertside.github.io/spring-mvc-start-archetype`
* Click next and create the project

Note: If you would like to create a project using archetype published in your local repository, skip repository field and make sure it is installed locally (see below).

## Eclipse / Spring Tool Suite

* Create new project `File > New > Maven Project`
* Make sure `Create a simple project` option is not selected
* Click `Next` to navigate to `Select an Archetype` screen
* Make sure `Include snapshot archetypes` is selected
* Click `Add Archetype` button
* Set `Archetype Group Id` to `com.github.dilbertside`
* Set `Archetype Artifact Id` to `spring-mvc-start`
* Set `Archetype Version` to `5.1.0`
* Set `Repository URL` to `http://dilbertside.github.io/spring-mvc-start-archetype`
* Click `OK` so the Archetype is added to the list
* Click `Next` and fill in `Group Id`, `Artifact Id` and `Version` of your new project

Note: Remember so select `Include snapshot archetypes`. 

If you have any troubles with installation in Eclipse, you may want to have a look at this issue: #74

## Local repository

### Install archetype locally self build

To install the archetype in your local repository execute the following commands:

```bash
  git clone https://github.com/dilbertside/spring-mvc-start-archetype.git
  cd spring-mvc-start-archetype
  mvn clean install
```
### Create a project from a local repository

Create a new empty directory for your project and navigate into it and then run:

```bash
  mvn archetype:generate \
      -DarchetypeGroupId=com.github.dilbertside \
      -DarchetypeArtifactId=spring-mvc-start \
      -DarchetypeVersion=5.1.0 \
      -DgroupId=com.example.acme \
      -DartifactId=acme \
      -Dversion=0.0.1
```

Note: The above command will bootstrap a project using the archetype published in your local repository.

# Run the project

## Docker

[Docker](https://docs.docker.com/install/#supported-platforms) must be installed

```bash
  mvn compile jib:buildTar
  docker load --input target/*.tar
  docker run acme:0.01-dev
```
## Eclipse IDE

create a new server goto Preferences -> Server -> Runtime Environments

and add the project just created, publish, start the server

## Tomcat plugin (deprecated)
Navigate to newly created project directory (`my-artifactId`) and then run:

```bash
  mvn test tomcat7:run
```

> Note: I do not recommend this way of running the artifact. Tomcat Maven Plugin Version 2.2 was released on 2013-11-11 

### Test in the browser
-------------------

  http://localhost:8080/

Note: No additional services are required in order to start the application. Mongo DB configuration is in place but it is not used in the code.



# Project Configuration 

## Switching to PostgreSQL

* Build and run with the correct profile:

```bash
mvn clean package -P-h2,dev,pgsql help:active-profiles
```

## Switching to H2 database

It is default DB activated, to change default reset to `<activeByDefault>false</activeByDefault>` in POM

* Build and run with the correct profile:

```bash
mvn clean package -P dev,h2 help:active-profiles
```

## Switching to HSQL DB

* Build and run with the correct profile:

```bash
mvn clean package -P-h2,dev,hsql help:active-profiles
```

## Enabling MongoDB repositories

* Open MongoConfig class and uncomment the following line:

```java
// @EnableMongoRepositories(basePackageClasses = Application.class)
```

Now you can add repositories to your project:

```java
@Repository
public interface MyRepository extends MongoRepository<MyDocument, String> {

}
```
