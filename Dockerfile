FROM openjdk:11

ADD target/bunker-app.jar bunker-app.jar
ENTRYPOINT ["java","-jar","bunker-app.jar"]

EXPOSE 8081