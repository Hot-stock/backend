FROM --platform=linux/amd64 openjdk:17

COPY src /app
WORKDIR /app

RUN cp /app/build/libs/gateway-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "./app.jar"]
