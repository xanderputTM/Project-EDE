FROM openjdk:21-jdk-slim-buster
EXPOSE 8085
ARG GOOGLE_CLIENTSECRET
ARG GOOGLE_CLIENTID
ARG GOOGLE_CLIENTSECRET=$GOOGLE_CLIENTSECRET
ARG GOOGLE_CLIENTID=$GOOGLE_CLIENTID
ADD target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]