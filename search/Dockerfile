FROM --platform=linux/amd64 openjdk:17-jdk-alpine

COPY . /app
WORKDIR /app

RUN cp /app/build/libs/search-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java","-jar","./app.jar"]
