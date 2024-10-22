# Java 17 기반 이미지 사용
FROM --platform=linux/amd64 openjdk:17-jdk-alpine

# 애플리케이션 파일 복사 및 작업 디렉토리 설정
COPY . /app
WORKDIR /app

# 빌드된 JAR 파일을 지정된 위치로 복사
RUN cp /app/build/libs/timeDeal-0.0.1-SNAPSHOT.jar /app/app.jar

# Pinpoint Agent 설정
ARG PINPOINT_VERSION=2.5.3
ARG AGENT_ID=timeDeal_agent
ARG APP_NAME=timeDeal
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
