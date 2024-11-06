FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts /app/

COPY . /app
RUN ./gradlew build -x test --parallel


FROM openjdk:21-jdk-slim AS app

WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar /app/app.jar

COPY .env /app

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app/app.jar"]
