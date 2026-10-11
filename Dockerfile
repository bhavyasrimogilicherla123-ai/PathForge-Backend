# Multi-stage Dockerfile for PathForge Spring Boot Backend
# Stage 1: Build stage with Maven and Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first to utilize Docker layer caching for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source directory and compile application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal Runtime stage with Java 17 JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create non-root system user for security
RUN addgroup -S pathforge && adduser -S pathforge -G pathforge
USER pathforge:pathforge

# Copy built executable jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render supplies PORT environment variable dynamically
ENV PORT=8080
EXPOSE 8080

# Launch Spring Boot application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
