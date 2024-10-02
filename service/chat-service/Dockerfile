FROM openjdk:17-silm
VOLUME /tmp
ADD ./build/libs/*.jar chat-service.jar

ENTRYPOINT ["java", "-jar", "/chat-service.jar"]