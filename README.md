# Pagely AI Service

> 사용자 경험을 바탕으로 AI를 활용한 서비스를 제공하는 서버입니다.
>

# 주요 기능

## 1. 개인 활동 기반 도서 추천 API

사용자의 독서 이력, 좋아요, 검색 패턴 등을 분석하여 벡터 임베딩 기반으로 도서를 추천합니다.

## 2. 독후감 기반 도서 요약 데이터 제공 API

사용자들이 작성한 독후감을 LLM으로 분석하여 도서별 핵심 요약 및 인사이트 데이터를 제공합니다.

# 기술 스택

| 분류 | 기술 |
| --- | --- |
| Framework | Spring Boot, Spring AI |
| Message Queue | Kafka |
| Database | PostgreSQL, pgvector |
| Monitoring | Prometheus, Grafana, EFK Stack |
| Infra | Docker, Docker Compose |

# 실행 방법

## 사전 요구사항

- Docker & Docker Compose 설치
- Java 17+
- `Pagely` 의 `infra-repo` 클론 필요

### 1. 인프라 실행

`infra-repo`  에서 데이터베이스 및 Kafka 관련 인프라를 실행합니다.

```java
# infra-repo 클론
git clone https://github.com/pagely-wisely/infra-repo
cd infra-repo

# PostgreSQL DB 컨테이너 실행
docker compose -f docker-compose.db.yaml up -d

# Kafka 실행
docker compose -f docker-compose.kafka.yaml up -d

# Kafka UI 포함
docker compose -f docker-compose.kafka.yaml --profile ui up -d              
```

### 2. 환경변수 설정

`src-main-resources` 경로에 `.env` 파일 생성 후 필요한 환경변수 값들을 넣어줍니다.

```java
DB_HOST=localhost

DB_AI_PORT=5433

DB_USERNAME={DB USERNAME}
DB_PASSWORD={DB PASSWORD}

AI_DB_NAME=pagely_ai

KAFKA_EXTERNAL_HOST=localhost
KAFKA_EXTERNAL_PORT={카프카 포트}

KAFKA_UI_PORT={카프카 UI포트}
OPENAI_KEY={OPENAI KEY}
```

### 3. `.gradle` 폴더 내에 github token 유저정보 입력

`Pagely` 서비스의 공통 모듈을 사용하기 위한 설정으로, `.gradle` 폴더 내에 `gradle.properties`  를 생성한 후 다음 정보를 입력합니다. 해당 토큰은 `read:packages` 에 대한 권한이 필수됩니다.

```java
gpr.user=YOUR_GITHUB_USERNAME
gpr.key=YOUR_GITHUB_TOKEN
```

# 아키텍처 개요

## AI 시스템 구성

<img width="1168" height="364" alt="Image" src="https://github.com/user-attachments/assets/59403fd9-4d20-45ec-8eff-d3955e6d30d8" />

## API 요청 흐름

### 독후감 기반 도서 요약 데이터 제공 API

<img width="906" height="163" alt="Image" src="https://github.com/user-attachments/assets/9490973e-ebd0-4d47-92c6-b5213c4fdd7a" />

### 개인 활동 기반 도서 추천 API

<img width="823" height="166" alt="Image" src="https://github.com/user-attachments/assets/c0d41aca-ad4e-46c8-9d21-d93a154b8fda" />

## Kafka 이벤트 흐름

<img width="958" height="169" alt="Image" src="https://github.com/user-attachments/assets/9fea4346-0713-4e06-8eed-60986680f979" />