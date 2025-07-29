FROM openjdk:17-jdk-slim
LABEL authors="1995c"

WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached if pom.xml unchanged)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application (this will detect your code changes)
RUN ./mvnw clean package -DskipTests

# Copy the built jar with a simpler name
RUN cp target/MsAouth-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]
