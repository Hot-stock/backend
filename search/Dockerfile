FROM --platform=linux/amd64 openjdk:21

COPY . /app
WORKDIR /app

RUN cp /app/build/libs/search-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "./app.jar"]
