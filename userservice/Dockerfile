# Build Stage
FROM --platform=linux/amd64 openjdk:17-jdk-alpine AS build

# Build environment setup
COPY . /app
WORKDIR /app

# Pinpoint Agent 설정
ARG PINPOINT_VERSION=2.5.3

# Pinpoint Agent 다운로드 및 압축 해제
RUN wget https://github.com/pinpoint-apm/pinpoint/releases/download/v${PINPOINT_VERSION}/pinpoint-agent-${PINPOINT_VERSION}.tar.gz \
    && tar xzf pinpoint-agent-${PINPOINT_VERSION}.tar.gz \
    && mv pinpoint-agent-${PINPOINT_VERSION} /pinpoint-agent

# 빌드된 JAR 파일을 지정된 위치로 복사
RUN cp /app/build/libs/userservice-0.0.1-SNAPSHOT.jar /app/app.jar

# Runtime Stage
FROM --platform=linux/amd64 openjdk:17-jdk-slim AS runtime

# Pinpoint Agent와 애플리케이션 JAR 복사
COPY --from=build /pinpoint-agent /pinpoint-agent
COPY --from=build /app/app.jar /app/app.jar

# AWS 환경 변수 설정
ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ARG AWS_DEFAULT_REGION

ENV AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
ENV AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
ENV AWS_DEFAULT_REGION=${AWS_DEFAULT_REGION}

# Pinpoint 설정 환경 변수 추가
ARG AGENT_ID=userservice_agent
ARG APP_NAME=userservice
ARG SPRING_PROFILES=dev

# JAVA_OPTS에 Pinpoint 설정 추가
ENV JAVA_OPTS="-javaagent:/pinpoint-agent/pinpoint-bootstrap.jar \
    -Dpinpoint.agentId=${AGENT_ID} \
    -Dpinpoint.applicationName=${APP_NAME} \
    -Dprofiler.transport.grpc.collector.ip=172.31.8.239 \
    -Dspring.profiles.active=${SPRING_PROFILES}"

# 애플리케이션 실행 명령어에 JAVA_OPTS 적용
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]

