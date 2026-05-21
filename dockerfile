FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
RUN chmod +x gradlew

COPY build.gradle settings.gradle ./
COPY src ./src

RUN ./gradlew clean bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/build/libs/app.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
