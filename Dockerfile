# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Run stage
FROM openjdk:17-slim
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/momentia-api/build/libs/*.jar app.jar

# Environment variables
ENV PORT=8080
ENV JAVA_OPTS=""

# Expose the application port
EXPOSE ${PORT}

# Set the startup command
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]