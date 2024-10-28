# 빌드 스테이지
FROM --platform=linux/amd64 openjdk:21 as builder

# 애플리케이션 파일 복사 및 작업 디렉토리 설정
COPY . /app
WORKDIR /app

# 필요한 패키지 설치 (microdnf 사용)
RUN set -eux; \
    microdnf install -y \
        gzip \
        tar \
        wget \
        unzip \
        curl \
    && microdnf clean all

# Pinpoint Agent 설정
ARG PINPOINT_VERSION=2.5.3

# Pinpoint Agent 다운로드 및 압축 해제
RUN wget https://github.com/pinpoint-apm/pinpoint/releases/download/v${PINPOINT_VERSION}/pinpoint-agent-${PINPOINT_VERSION}.tar.gz \
    && tar xzf pinpoint-agent-${PINPOINT_VERSION}.tar.gz \
    && mv pinpoint-agent-${PINPOINT_VERSION} /pinpoint-agent

# JAR 파일 준비
RUN cp /app/build/libs/search-0.0.1-SNAPSHOT.jar /app/app.jar

# AWS CLI 설치
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" \
    && unzip awscliv2.zip \
    && ./aws/install \
    && rm -rf awscliv2.zip aws

# AWS 환경 변수 설정
ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ARG AWS_DEFAULT_REGION

ENV AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
ENV AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
ENV AWS_DEFAULT_REGION=${AWS_DEFAULT_REGION}

# 실행 스테이지
FROM --platform=linux/amd64 openjdk:21

# AWS CLI 복사
COPY --from=builder /usr/local/aws-cli /usr/local/aws-cli
ENV PATH="/usr/local/aws-cli/v2/current/bin:$PATH"

COPY --from=builder /pinpoint-agent /pinpoint-agent

# 애플리케이션 JAR 파일 복사
COPY --from=builder /app/app.jar /app/app.jar

# JAVA_OPTS에 Pinpoint 설정 추가
ARG AGENT_ID=search_agent
ARG APP_NAME=search
ARG SPRING_PROFILES=dev
ENV JAVA_OPTS="-javaagent:/pinpoint-agent/pinpoint-bootstrap.jar \
    -Dpinpoint.agentId=${AGENT_ID} \
    -Dpinpoint.applicationName=${APP_NAME} \
    -Dprofiler.transport.grpc.collector.ip=172.31.8.239 \
    -Dspring.profiles.active=${SPRING_PROFILES}"

# 애플리케이션 실행 명령어에 JAVA_OPTS 적용
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]
