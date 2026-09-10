FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Copy Java source files
COPY *.java .

# Copy application configuration
COPY application.properties .

# Copy frontend files
COPY *.html .
COPY *.css .
COPY *.js .

# Build Spring Boot application
RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy generated Spring Boot JAR
COPY --from=build /app/target/memorylink-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
