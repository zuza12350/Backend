FROM openjdk:11-jdk-slim

COPY target/Engineer-1.0-SNAPSHOT.jar Engineer-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/Engineer-1.0-SNAPSHOT.jar"]