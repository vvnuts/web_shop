FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . /app/.
COPY pom.xml /
RUN mvn -f /app/pom.xml clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /target/*.jar /app/*.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/*.jar"]
