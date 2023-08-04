# Use the official maven/Java 8 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3.8.4-openjdk-11 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Use OpenJDK for our runtime environment
FROM openjdk:11-jre-slim

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/webbrowsergame-0.0.1-SNAPSHOT.jar /webbrowsergame.jar

# Specifies a command that provides default values for an executing container.
CMD ["java","-jar","/webbrowsergame.jar"]
