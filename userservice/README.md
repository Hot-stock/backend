
# 유저 서비스 (User Service)

## 목차
- [소개](#소개)
- [기술 스택](#기술-스택)
- [시작하기](#시작하기)
- [프로젝트 구조](#프로젝트-구조)
- [기능](#기능)
- [API 문서](#api-문서)
- [기여하기](#기여하기)
- [라이센스](#라이센스)

## 소개
유저 서비스는 모노레포 아키텍처의 일부로, 회원 가입, 로그인, 로그아웃 등의 유저 관련 기능을 관리합니다. 이 서비스는 Java Spring Boot를 사용하여 개발되었으며, REST 원칙을 따릅니다.

## 기술 스택
- Java 17
- Spring Boot 3.3.1
- JWT (토큰 기반 인증)
- Docker (컨테이너화)

## 시작하기

### 사전 준비 사항
- JDK 17 이상
- Docker (선택 사항, 컨테이너화를 위해)
- Gradle (빌드 도구)

### 설치
레포지토리를 클론한 후 `userservice` 디렉토리로 이동하세요:

```bash
git clone https://github.com/Hot-stock/backend.git
./gradlew build
```

### 실행
```bash
cd backend/userservice
docker compose up
```

## 프로젝트 구조
프로젝트의 주요 디렉토리 구조는 다음과 같습니다:

```
.
├── main
│   ├── java
│   │   └── com
│   │       └── bjcareer
│   │           └── userservice
│   │               ├── application
│   │               │   ├── auth
│   │               │   │   ├── aop
│   │               │   │   └── token
│   │               │   │       ├── exceptions
│   │               │   │       └── valueObject
│   │               │   ├── ports
│   │               │   │   ├── in
│   │               │   │   └── out
│   │               │   └── register
│   │               ├── common
│   │               ├── domain
│   │               │   └── entity
│   │               │       └── exceptions
│   │               ├── in
│   │               │   └── api
│   │               │       ├── exHandler
│   │               │       ├── request
│   │               │       └── response
│   │               ├── interceptor
│   │               └── out
│   │                   └── persistance
│   │                       └── repository
│   │                           ├── exceptions
│   │                           └── queryConst
│   │   
```

## 기능
- 회원 가입
- 로그인
- 로그아웃
- 토큰 갱신

## API 문서
API 문서는 Swagger UI와 OpenAPI 스펙을 통해 확인할 수 있습니다.
- [OpenAPI 스펙 (swagger.yaml)](./docs/swagger.yaml)

## 기여하기
이 프로젝트에 기여하고 싶으시면, 이슈를 생성하거나 풀 리퀘스트를 제출해 주세요. 기여 과정을 따르기 위한 세부 사항은 아래와 같습니다:

1. 포크(Fork) 하세요.
2. 새 브랜치를 생성하세요 (`git checkout -b feature/새로운-기능`).
3. 변경 사항을 커밋하세요 (`git commit -m '새로운 기능 추가'`).
4. 브랜치에 푸시하세요 (`git push origin feature/새로운-기능`).
5. 풀 리퀘스트를 생성하세요.

## 라이센스
이 프로젝트는 MIT 라이센스를 따릅니다. 자세한 내용은 `LICENSE` 파일을 참조하세요.
