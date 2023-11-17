FROM openjdk:21-jdk-slim-buster
EXPOSE 8082
ADD target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]