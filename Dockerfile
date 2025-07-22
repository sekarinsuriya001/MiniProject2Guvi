#
# Build stage
#
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean install

#
# Package stage
#
FROM eclipse-temurin:21-jdk
COPY --from=build /target/empManagementBackend-0.0.1-SNAPSHOT.jar demo.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]