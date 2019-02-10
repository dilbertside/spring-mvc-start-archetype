#set( $dollar = '$' )
#set( $pound = '#' )
#set( $escape = '\' )
FROM tomcat:8.5-alpine
ARG PRJ_VERSION=@project.version@
ARG PRJ_NAME="@project.name@"
ARG TZ
LABEL description="This image is used to start the demo ${dollar}{PRJ_NAME} war web " vendor="DBS" version="${dollar}{PRJ_VERSION}" maintainer="DBS"

RUN rm -fr /usr/local/tomcat/webapps/*

COPY @project.artifactId@-${dollar}PRJ_VERSION.war /usr/local/tomcat/webapps/@project.artifactId@.war

ENV JAVA_TOOL_OPTIONS="-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom @docker.java.extra.opts@"
ENV TZ ${dollar}TZ
ENV LC_CTYPE en_US.UTF-8
EXPOSE 8080
EXPOSE 8009
VOLUME ["/usr/local/tomcat/logs", "/usr/local/tomcat/db"]