# First stage: Build
FROM maven:3.8-openjdk-17 as builder

COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

CMD ["java", "-jar", "/target/gamerealm-0.0.1-SNAPSHOT.jar"]

