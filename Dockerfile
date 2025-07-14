FROM eclipse-temurin:21-jdk-alpine
ARG JAR_File=target/*.jar
COPY ./target/card_cost_api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]