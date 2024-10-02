FROM openjdk:17-silm
VOLUME /tmp
ADD ./build/libs/*.jar eureka-service.jar

ENTRYPOINT ["java", "-jar", "/eureka-service.jar"]