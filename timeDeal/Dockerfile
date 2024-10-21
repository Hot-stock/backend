FROM --platform=linux/amd64 openjdk:17-jdk-alpine

COPY . /app
WORKDIR /app

RUN cp /app/build/libs/timeDeal-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","./app.jar"]
