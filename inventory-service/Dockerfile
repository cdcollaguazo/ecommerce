FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app
COPY --from=build /build/target/inventory-service-*.jar ./inventory-service.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=cloud", "inventory-service.jar"]
