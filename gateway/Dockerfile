# Java 17 기반 이미지 사용
FROM --platform=linux/amd64 openjdk:17-jdk-alpine AS build

# Build environment setup
COPY . /app
WORKDIR /app

# Pinpoint Agent 설정
ARG PINPOINT_VERSION=2.5.3

# 필요한 패키지 설치 및 Pinpoint Agent 다운로드
RUN apk add --no-cache wget tar \
    && wget https://github.com/pinpoint-apm/pinpoint/releases/download/v${PINPOINT_VERSION}/pinpoint-agent-${PINPOINT_VERSION}.tar.gz \
    && tar xzf pinpoint-agent-${PINPOINT_VERSION}.tar.gz \
    && mv pinpoint-agent-${PINPOINT_VERSION} /pinpoint-agent \
    && rm -f pinpoint-agent-${PINPOINT_VERSION}.tar.gz

# 빌드된 JAR 파일을 지정된 위치로 복사
RUN cp /app/build/libs/gateway-0.0.1-SNAPSHOT.jar /app/app.jar

# Runtime Stage
FROM --platform=linux/amd64 openjdk:17-jdk-slim AS runtime

# AWS CLI 설치
RUN apt-get update && \
    apt-get install -y curl unzip && \
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    ./aws/install && \
    rm -rf awscliv2.zip aws && \
    apt-get clean

# AWS 환경 변수 설정
ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ARG AWS_DEFAULT_REGION

ENV AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
ENV AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
ENV AWS_DEFAULT_REGION=${AWS_DEFAULT_REGION}

# 빌드 스테이지에서 필요한 파일 복사
COPY --from=build /pinpoint-agent /pinpoint-agent
COPY --from=build /app/app.jar /app/app.jar

ARG AGENT_ID=gateway-agent
ARG APP_NAME=gateway
ARG SPRING_PROFILES=dev

# JAVA_OPTS에 Pinpoint 설정 추가
ENV JAVA_OPTS="-javaagent:/pinpoint-agent/pinpoint-bootstrap.jar \
    -Dpinpoint.agentId=${AGENT_ID} \
    -Dpinpoint.applicationName=${APP_NAME} \
    -Dprofiler.transport.grpc.collector.ip=172.31.8.239 \
    -Dspring.profiles.active=${SPRING_PROFILES}"

# 애플리케이션 실행 명령어에 JAVA_OPTS 적용
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]
