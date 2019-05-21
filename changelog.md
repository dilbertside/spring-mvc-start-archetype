## 5.1.9

* bump BoM to 5.1.11 with Spring framework, boot, data upgrade

## 5.1.8

* bump BoM to 5.1.10 with Spring framework, boot, data, security upgrade

## 5.1.7

* bump BoM to 5.1.8 with Spring framework, boot, data, security upgrade

## 5.1.6

* refactor JPA configuration to detect dialect if not set
* really use embedded database for unit test
* remove maven profile test (useless with refactoring)
* add configuration to deploy on Heroku for demo application
* use @EventListener instead of implements ApplicationListener
* increase test coverage to 51% with coveralls, implement HomeControllerTest, AboutControllerTest, SigninControllerTest, ErrorControllerTest (total 21 tests)
* add Spotify docker image assembly recipe
* make a ready made Docker Hub image for trial

## 5.1.5

* migrate from JUnit 4 to JUnit 5 
* make parameterizable the selection of BoM parent and default java version
* add readme recipe docker and add timezone for docker jib
* add maven MySql DB profile


## 5.1.4

* bump BoM to 5.1.5

## 5.1.3

* Documentation improvements

## 5.1.2

* add default value for archetype generation

## 5.1.0

* import from kolorobot/spring-mvc-quickstart-archetype
* Refactoring with Spring 5.1.4 as baseline