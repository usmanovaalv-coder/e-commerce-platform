FROM openjdk:23-jdk-slim

WORKDIR /app

COPY target/auth-service-0.0.1-SNAPSHOT.jar auth-service.jar

EXPOSE 8080

CMD ["java", "-jar", "auth-service.jar"]