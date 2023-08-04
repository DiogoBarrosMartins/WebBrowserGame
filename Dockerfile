# Use the official maven/Java 11 image to create a build artifact.
# This is based on Debian and sets the MAVEN_HOME environment variable
FROM maven:3.8-openjdk-11 as builder

# Set the working directory
WORKDIR /app

# Copy pom.xml and src directory to the docker image
COPY pom.xml .
COPY src ./src

# Install git
RUN apt-get update && \
    apt-get install -y git

# Build a release artifact
RUN mvn package -DskipTests

# Use OpenJDK 11 for our runtime environment
FROM openjdk:11

# Set the working directory
WORKDIR /app

# Copy the jar file from builder image to the new docker image
COPY --from=builder /app/target/gamerealm-0.0.1-SNAPSHOT.jar /app

# Set the startup command
CMD ["java", "-jar", "/app/gamerealm-0.0.1-SNAPSHOT.jar"]
