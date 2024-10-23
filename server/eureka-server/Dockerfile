FROM openjdk:17-slim
VOLUME /tmp
ARG JAR_FILE=build/libs/eureka-server.jar
ARG CONFIG_PATH=src/main/resources/application.yml

COPY ${JAR_FILE} /app/eureka-server.jar
COPY ${CONFIG_PATH} /eureka/application.yml
EXPOSE 8888
ENTRYPOINT ["java", "-Dspring.config.location=/eureka/application.yml", "-jar", "/app/eureka-server.jar"]