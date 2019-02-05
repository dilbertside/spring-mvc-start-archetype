#prerequisites

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
docker run -it --rm -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven diside/spring-mvc-start-archetype-docker:5.1.4 mvn clean package
```

## locally

Prerequisites: Java and [Apache Maven](https://maven.apache.org/download.cgi) must be installed.

```bash
mvn clean package
```

# Unit Test

## Docker

Prerequisites: [Docker](https://docs.docker.com/install/#supported-platforms) must be installed

```bash
docker run -it --rm -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven diside/spring-mvc-start-archetype-docker:5.1.4 mvn test -Ptest
```

## locally

Prerequisites: Java and [Apache Maven](https://maven.apache.org/download.cgi) must be installed.

```bash
mvn test -Ptest
```

# Run 

## Web app server

Drop the war built in any Web application container

Example
### Tomcat 9 https://tomcat.apache.org/download-90.cgi 
copy war in webapps directory and start 

```bash
cd apache-tomcat-9.0.14
bin/startup.sh && tail -n80 -f logs/catalina.out
```
## Docker container

it uses Google [jib-maven-plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin#war-projects) which does not require to have docker installed on the build machine

### to a remote repo

```bash
mvn compile jib:build
```
### export Dockerfile and build against Docker daemon

```bash
mvn jib:exportDockerContext
mvn jib:dockerBuild
```
or with a maven container 

```bash
docker run -it --rm -v "$(pwd)":/usr/src/mymaven -v /var/run/docker.sock:/var/run/docker.sock -w /usr/src/mymaven diside/spring-mvc-start-archetype-docker:latest sh -c "mvn compile jib:dockerBuild"
```


### save image container as archive

```bash
mvn compile jib:buildTar
docker load --input target/*.tar
```




