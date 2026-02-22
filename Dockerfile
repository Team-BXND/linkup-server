FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .
COPY gradle ./gradle
COPY gradlew .
COPY gradlew.bat .
COPY src ./src

RUN ./gradlew clean bootJar

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]