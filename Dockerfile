# Use official openjdk base image for Java 21
FROM openjdk:21-jdk-slim AS build

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and dependencies
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy the rest of the project
COPY src src

# Build the Spring Boot application with Gradle
RUN ./gradlew build -x test

# Final image with the JAR file
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
