Spring Web MVC 5.1 start Maven Archetype
======================================

# Status

[![Release](https://jitpack.io/v/dilbertside/spring-mvc-start-archetype.svg)](https://jitpack.io/#dilbertside/spring-mvc-start-archetype)
[![Build Status](https://travis-ci.org/dilbertside/spring-mvc-start-archetype.svg)](https://travis-ci.org/dilbertside/spring-mvc-start-archetype) 

## Try it out!

[Demo application](https://spring-mvc-start-demo.herokuapp.com/) on [Heroku](https://www.heroku.com/home). [demo code](https://github.com/dilbertside/spring-mvc-start-archetype-demo)

Application might be sleeping, please wait few seconds as it runs on [free plan](https://devcenter.heroku.com/articles/free-dyno-hours#dyno-sleeping), Thank you.

# Summary

The project is a Maven archetype for Spring MVC web application.
It is inspired from https://github.io/kolorobot/spring-mvc-quickstart-archetype and was originally a fork.
kolorobot archetype is based onto Spring Framework 5.0.x (as of January 2019)

After refactoring a bit to upgrade Spring to 5.1, the aim became to build a full Spring Framework MVC  Web application only (which should not contain any Spring Boot dependencies). In that purpose some extra configuration have been added Cache, Async, WebJars, Problem.

Why?

While Spring Boot is a wonderful way to build a web application in no time, this archetype startup is a way to claim back control to all the opinionated choices made by Spring Boot team.
In that way, the configuration is not hidden in Auto Configuration properties. Saying that, reading Boot code helps to build a configuration without Spring Boot. For a complete Spring Boot Application you may want to have a look to [JHipster](https://www.jhipster.tech/)

This starter can be used for 
* educational purpose
* migration purpose (ie from Spring Framework core 4.3.x to 5.1.x)
* as a starting point to migrate from Spring Boot to Spring Framework core and Web MVC.
* ...

Notes:
This archetype may not work with prior versions of Spring Framework 5.0.x, or 4.3.x, and Java 1.8
It uses methods which are not reverse compatible.

When Spring Framework 5.2 will be released, a new branch to track progress will be created.

PR contributions are welcome as long as there is no Spring Boot dependency included.

[ChangeLog](https://raw.githubusercontent.com/dilbertside/spring-mvc-start-archetype/master/changelog.md)

## Generated project characteristics
* Bill of Material
* Spring MVC web application, with Java config and no Spring Boot
* [Thymeleaf](http://www.thymeleaf.org/), [Bootstrap](https://getbootstrap.com/) with [WebJars](http://webjars.org)
* Java Persistence (JPA)
** [Spring Data JPA](https://spring.io/projects/spring-data)
** [Hibernate](http://hibernate.org/orm)
** [HSQLDB](http://hsqldb.org/)
** [H2](http://www.h2database.com/)
** [Postgresql](https://www.postgresql.org/)
* Testing with [JUnit](http://junit.org )/[Mockito](https://github.com/mockito/mockito)
* [Spring Security](https://spring.io/projects/spring-security) 
* [MongoDB](http://www.mongodb.org/) (Spring Data Mongo)
* Docker containerization with [Google Jib maven plugin](https://github.com/GoogleContainerTools/jib)
* Cache memory with [Caffeine](https://github.com/ben-manes/caffeine)
* Email: Google mail ready
* [Jackson JSON/XML/CSV data-binding functionality](http://github.com/FasterXML/jackson)
* [Problem, implementation of the application/problem+json draft](https://github.com/zalando/problem)
* [Lombok](https://projectlombok.org/) to reduce boilerplate code (cf readme of generated project)
* Unit tests with [JUnit 5 Jupiter](https://junit.org/junit5/) and [Mockito](https://github.com/mockito/mockito)

## TODO
* Localization improvements
* View.JS integration

## Prerequisites

- JDK 8 (tested with Java 11)
- [Apache Maven](https://maven.apache.org/download.cgi) 3.5 (tested with 3.6.0)
- Not mandatory [Docker](https://docs.docker.com/install/#supported-platforms) 


# Demo project

A demo generated artifact is available at [spring-mvc-start-archetype-demo repo](https://github.com/dilbertside/spring-mvc-start-archetype-demo)

```bash 
git clone https://github.com/dilbertside/spring-mvc-start-archetype-demo.git spring-mvc-webapp-demo
cd spring-mvc-webapp-demo
mvn package -Ph2
```

## [Heroku](https://www.heroku.com/home) Live

[Demo application](https://spring-mvc-start-demo.herokuapp.com/)

Application might be sleeping, please wait few seconds as it runs on [free plan](https://devcenter.heroku.com/articles/free-dyno-hours#dyno-sleeping), Thank you.

## Docker Hub image

A demo webapp is generated from the defaults of this archetype generator and a containerized image is built in [Docker Hub](https://hub.docker.com/r/diside/webapp)

Get it running in no time with following command:

```bash 
docker run -it --rm -p 8080:8080 diside/webapp

```


# Create a new project with this Maven archetype

## Docker maven

Reusing the Docker Maven repository and archetype generating project in Interactive mode

### Script
Download and use [Bash generation script](https://raw.githubusercontent.com/dilbertside/spring-mvc-start-archetype-docker/master/generate.sh) and run 

```bash 
bash generate.sh
```

### Manual

Beware: no pom.xml should be in directory where you run following command.

```bash

docker run -it --rm -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven diside/spring-mvc-start-archetype-docker:5.1.4 mvn archetype:generate \
      -DarchetypeGroupId=com.github.dilbertside \
      -DarchetypeArtifactId=spring-mvc-start-archetype \
      -DarchetypeVersion=5.1.7
```

## command line

Prerequisites: Java and Maven must be installed.

add to your ~/.m2/settings.xml a new repo in an activated profile

```
<repositories>
  ...
  <repository>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
  ...
<repositories>  
```
and run interactively in a directory without a pom.xml in it

```bash
  mvn archetype:generate \
      -DarchetypeGroupId=com.github.dilbertside \
      -DarchetypeArtifactId=spring-mvc-start-archetype \
      -DarchetypeVersion=5.1.7
```
Note: [script](https://github.com/dilbertside/spring-mvc-start-archetype/blob/master/test.sh) to test generator prior to release might be useful to peruse

## IntelliJ

* Create new project `File > New > Project`
* Click Maven on the left hand side of the new project dialog
* Check `Create from archetype`
* Click the `Add Archetype` button
* Set `Group Id` to `com.github.dilbertside`
* Set `Artifact Id` to `spring-mvc-start-archetype`
* Set `Version` to `5.1.7`
* Set `Repository` to `https://dilbertside.github.io/spring-mvc-start-archetype`
* Click next and create the project

Note: If you would like to create a project using archetype published in your local repository, skip repository field and make sure it is installed locally (see below).

## Eclipse / Spring Tool Suite

* Create new project `File > New > Maven Project`
* Make sure `Create a simple project` option is not selected
* Click `Next` to navigate to `Select an Archetype` screen
* Make sure `Include snapshot archetypes` is selected
* Click `Add Archetype` button
* Set `Archetype Group Id` to `com.github.dilbertside`
* Set `Archetype Artifact Id` to `spring-mvc-start-archetype`
* Set `Archetype Version` to `5.1.7`
* Set `Repository URL` to `https://dilbertside.github.io/spring-mvc-start-archetype`
* Click `OK` so the Archetype is added to the list
* Click `Next` and fill in `Group Id`, `Artifact Id` and `Version` of your new project

Note: Remember to select `Include snapshot archetypes`. 


## Local repository

### Install archetype locally self build

To install the archetype in your local repository execute the following commands:

```bash
  git clone https://github.com/dilbertside/spring-mvc-start-archetype.git
  cd spring-mvc-start-archetype
  mvn clean install
```
### Create a project from a local repository

Use a directory without a pom.xml in it and then run:

```bash
  mvn archetype:generate \
      -DarchetypeGroupId=com.github.dilbertside \
      -DarchetypeArtifactId=spring-mvc-start-archetype \
      -DarchetypeVersion=5.1.7
```

Note: The above command will bootstrap a project using the archetype published in your local repository.

# Run the project

## Docker

[Docker](https://docs.docker.com/install/#supported-platforms) must be installed

```bash
  mvn compile jib:buildTar
  docker load --input target/*.tar
  docker run acme:0.0.1-dev
```
## Eclipse IDE

create a new server goto Preferences -> Server -> Runtime Environments

and add the project just created, publish, start the server

## Tomcat plugin (deprecated)
Navigate to newly created project directory (`my-artifactId`) and then run:

```bash
  mvn test tomcat7:run
```

> Note: It is not recommended to run the artifact, Tomcat Maven Plugin Version 2.2 was released on 2013-11-11

### Test in the browser
-------------------

  http://localhost:8080/

Note: No additional services are required in order to start the application. Mongo DB configuration is in place but it is not used in the code.



# Project Configuration 

## Switching to PostgreSQL

Prerequisites:
 PostgreSQL up and running, tablespace, and credentials valid

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

## Switching to MySQL/Maria DB

Prerequisites:
 MySQL up and running, tablespace, and credentials valid
 
* Build and run with the correct profile:

```bash
mvn clean package -P-h2,dev,mysql help:active-profiles
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

# License

This project is licensed under the MIT License - see the [file](LICENSE)  for details
