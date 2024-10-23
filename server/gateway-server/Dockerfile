FROM openjdk:17-slim

# JAR 및 YAML 파일 복사
COPY build/libs/gateway-server-0.0.1-SNAPSHOT.jar /app/gateway-server.jar
COPY build/resources/main/application.yaml /gateway/application.yaml

# 포트 노출
EXPOSE 8000

# 애플리케이션 실행 명령
ENTRYPOINT ["java", "-jar", "/app/gateway-server.jar"]

