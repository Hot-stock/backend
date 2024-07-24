FROM --platform=linux/amd64 openjdk:17-jdk-alpine

COPY . /app
WORKDIR /app

RUN ./gradlew init
RUN ./gradlew build -x test

COPY ./build/libs/server-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java","-jar","./app.jar"]
