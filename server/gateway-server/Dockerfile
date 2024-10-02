FROM openjdk:17-silm
VOLUME /tmp
ADD ./build/libs/*.jar gateway-service.jar

ENTRYPOINT ["java", "-jar", "/gateway-service.jar"]