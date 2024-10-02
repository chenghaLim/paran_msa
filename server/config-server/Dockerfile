FROM openjdk:17-slim
VOLUME /tmp
ADD ./build/libs/*.jar config-service.jar

ENTRYPOINT ["java", "-jar", "/config-service.jar"]