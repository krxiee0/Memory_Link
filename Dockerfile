FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/memorylink-0.0.1-SNAPSHOT.jar"]
