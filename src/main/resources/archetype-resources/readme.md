#prerequisites

- JDK 8
- Maven 3.5

## Project Lombok
This project uses Project Lombok https://projectlombok.org/ to reduce bloated class property accessors

To benefit in your IDE the Lombok project 
### Eclipse install
https://projectlombok.org/setup/eclipse
### IntelliJ install
IDEA https://projectlombok.org/setup/intellij

# Build

```bash
mvn clean package
```

# Test

```bash
mvn test
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
it uses Google Jib which does not require to have docker installed on the build machine
 
https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin#war-projects

### to a remote repo
```bash
mvn compile jib:build
```
### export Dockerfile and build against Docker daemon

```bash
mvn jib:exportDockerContext
mvn jib:dockerBuild
```

### save image container as archive

```bash
mvn compile jib:buildTar
docker load --input target/*.tar
```




