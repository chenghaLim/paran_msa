FROM openjdk:17-silm
VOLUME /tmp
ADD ./build/libs/*.jar comment-service.jar

ENTRYPOINT ["java", "-jar", "/comment-service.jar"]