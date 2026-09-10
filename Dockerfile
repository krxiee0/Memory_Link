FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Copy all Java source files from the repository root
COPY *.java .

# Build Spring Boot application
RUN mvn clean package -DskipTests

# ---------------------------------

FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy generated Spring Boot JAR
COPY --from=build /app/target/memorylink-0.0.1-SNAPSHOT.jar app.jar

# Render uses its own PORT environment variable
EXPOSE 8080

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]
