# Build Stage
FROM --platform=linux/amd64 openjdk:17 AS build

# Build environment setup
COPY . /app
WORKDIR /app

# Package Stage
FROM --platform=linux/amd64 openjdk:17-jdk-slim

# Copy the jar from the build stage
COPY --from=build /app/build/libs/userservice-0.0.1-SNAPSHOT.jar /app/app.jar

# Set the working directory
WORKDIR /app

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "./app.jar"]
