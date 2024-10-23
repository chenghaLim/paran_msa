FROM openjdk:17-slim

# JAR 및 YAML 파일 복사
COPY build/libs/gateway-server-0.0.1-SNAPSHOT.jar /app/gateway-server.jar
COPY build/resources/main/application.yaml /gateway/application.yaml

# 포트 노출
EXPOSE 8888

# 애플리케이션 실행 시 YAML 파일을 설정 경로로 사용
ENTRYPOINT ["java", "-Dspring.gateway.location=/gateway/application.yaml", "-jar", "/app/gateway-server.jar"]
