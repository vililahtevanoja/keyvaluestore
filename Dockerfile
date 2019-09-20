FROM openjdk:12 AS builder
WORKDIR /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN ./gradlew clean compileJava compileTestJava || return 0
COPY . .
RUN ./gradlew bootJar

FROM openjdk:12-alpine
COPY --from=builder /app/build/libs/keyvaluestore*.jar app.jar
CMD ["java", "-jar", "app.jar"]
