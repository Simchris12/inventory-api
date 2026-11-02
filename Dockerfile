FROM ubuntu:latest
LABEL authors="Cristofer_Castillo"

ENTRYPOINT ["top", "-b"]

#Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml
COPY src ./src
RUN mvn clean package -DskipTest

#Stage 2: Run the application
FROM eclipse-temurin:21-jre-apline
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]