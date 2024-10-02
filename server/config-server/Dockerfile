FROM openjdk:17-silm
VOLUME /tmp
ADD ./build/libs/*.jar config-service.jar

ENTRYPOINT ["java", "-jar", "/config-service.jar"]