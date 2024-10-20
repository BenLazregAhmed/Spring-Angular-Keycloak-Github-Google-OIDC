FROM openjdk:21-rc-oraclelinux8
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]