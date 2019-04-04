# Prerequisites

- JDK 8
- [Apache Maven](https://maven.apache.org/download.cgi) 3.5

## Project Lombok
This project uses Project [Lombok](https://projectlombok.org/) https://projectlombok.org/ to reduce bloated class property accessors

To benefit in your IDE the Lombok project 

### Eclipse install

https://projectlombok.org/setup/eclipse

### IntelliJ install

IDEA https://projectlombok.org/setup/intellij

# Build

## Docker

Prerequisites: [Docker](https://docs.docker.com/install/#supported-platforms) must be installed

```bash
docker run -it --rm -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven diside/spring-mvc-start-archetype-docker:5.1.8 mvn clean package
```

## locally

Prerequisites: Java and [Apache Maven](https://maven.apache.org/download.cgi) must be installed.

```bash
mvn clean package -Ph2
```
with default dev profile activated and H2 type DB

# Unit Test

## Docker

Prerequisites: [Docker](https://docs.docker.com/install/#supported-platforms) must be installed

```bash
docker run -it --rm -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven diside/spring-mvc-start-archetype-docker:5.1.8 mvn clean test
```

## locally

Prerequisites: Java and [Apache Maven](https://maven.apache.org/download.cgi) must be installed.

```bash
mvn clean test
```

# Run 

## Web app server

Drop the war built in any Web application container

Example

### Tomcat 9 https://tomcat.apache.org/download-90.cgi 

  copy war in `webapps` directory and start 

```bash
cd apache-tomcat-9.0.14
bin/startup.sh && tail -n80 -f logs/catalina.out
```
## Docker container

### Spotify docker builder

```bash
mvn clean install -Pdocker,dev,h2
docker run -it --rm -p 8080:8080 diside/webapp:latest
```
browse to http://localhost:8080/webapp/

Change environment profile "dev", database type profile "h2" as required and eventually "diside/webapp:1.0.0-dev" if default values were changed

### JIB

It uses Google [jib-maven-plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin#war-projects) which does not require to have docker installed on the build machine


```bash
mvn install -Pjib
```
or with a maven container 

```bash
docker run -it --rm -v "$(pwd)":/usr/src/mymaven -v /var/run/docker.sock:/var/run/docker.sock -w /usr/src/mymaven diside/spring-mvc-start-archetype-docker:latest sh -c "mvn install -Pjib"
```
