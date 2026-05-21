FROM gradle:8.7-jdk21 AS build

WORKDIR /app

# 캐시 최적화
COPY build.gradle ./
COPY settings.gradle ./
COPY gradle.properties ./

# 전체 복사
COPY . .

# wrapper 사용 안함
RUN gradle clean build -x check -x test -Pproduction

# ===== Run Stage =====
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java","-jar","app.jar"]
