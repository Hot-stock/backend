# Build Stage
FROM --platform=linux/amd64 openjdk:17 AS build

# Build environment setup
COPY . /app
WORKDIR /app

# Package Stage
FROM --platform=linux/amd64 openjdk:17-jdk-slim

# Copy the jar from the build stage
COPY --from=build /app/build/libs/userservice-0.0.1-SNAPSHOT.jar /app/app.jar

# Pinpoint Agent 설정
ARG PINPOINT_VERSION=2.5.3
ARG AGENT_ID=userservice_agent
ARG APP_NAME=userservice
ARG SPRING_PROFILES=dev

# Pinpoint Agent 다운로드 및 압축 해제
RUN wget https://github.com/pinpoint-apm/pinpoint/releases/download/v${PINPOINT_VERSION}/pinpoint-agent-${PINPOINT_VERSION}.tar.gz \
    && tar xzf pinpoint-agent-${PINPOINT_VERSION}.tar.gz \
    && mv pinpoint-agent-${PINPOINT_VERSION} /pinpoint-agent

# JAVA_OPTS에 Pinpoint 설정 추가
ENV JAVA_OPTS="-javaagent:/pinpoint-agent/pinpoint-bootstrap.jar \
    -Dpinpoint.agentId=${AGENT_ID} \
    -Dpinpoint.applicationName=${APP_NAME} \
    -Dprofiler.transport.grpc.collector.ip=${COLLECTOR_IP} \
    -Dspring.profiles.active=${SPRING_PROFILES}"

# 애플리케이션 실행 명령어에 JAVA_OPTS 적용
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]
