# Todo 애플리케이션 프로젝트 구조

## 📋 프로젝트 개요
Spring Boot + Kotlin 기반의 세션 인증을 사용하는 Todo 관리 웹 애플리케이션

## 🛠 기술 스택
- **Backend**: Spring Boot 3.x, Kotlin
- **Database**: H2 Database (In-Memory)
- **ORM**: Spring Data JPA
- **Security**: Spring Security (BCrypt 암호화)
- **Frontend**: Thymeleaf
- **Build Tool**: Gradle (Kotlin DSL)

## 📁 프로젝트 구조

```
todo/
├── build.gradle.kts                # Gradle 빌드 설정
├── settings.gradle.kts             # Gradle 프로젝트 설정
├── gradlew                         # Gradle Wrapper (Unix)
├── gradlew.bat                     # Gradle Wrapper (Windows)
├── HELP.md                         # Spring Boot 도움말
├── PROJECT_STRUCTURE.md            # 프로젝트 구조 문서
│
├── gradle/wrapper/                 # Gradle Wrapper 파일들
│   ├── gradle-wrapper.jar
│   └── gradle-wrapper.properties
│
├── build/                          # 빌드 결과물 (자동 생성)
│
└── src/
    ├── main/
    │   ├── kotlin/com/example/todo/
    │   │   ├── TodoApplication.kt          # Spring Boot 메인 클래스
    │   │   │
    │   │   ├── config/
    │   │   │   └── SecurityConfig.kt       # Spring Security 설정
    │   │   │
    │   │   ├── controller/
    │   │   │   ├── AuthController.kt       # 인증 관련 컨트롤러 (회원가입/로그인)
    │   │   │   ├── TodoController.kt       # Todo REST API 컨트롤러
    │   │   │   └── TodoWebController.kt    # Todo 웹 페이지 컨트롤러
    │   │   │
    │   │   ├── dto/
    │   │   │   ├── TodoRequest.kt          # Todo 요청 DTO
    │   │   │   ├── TodoResponse.kt         # Todo 응답 DTO
    │   │   │   └── UserRequest.kt          # 사용자 요청 DTO (회원가입/로그인)
    │   │   │
    │   │   ├── entity/
    │   │   │   ├── Todo.kt                 # Todo JPA 엔티티
    │   │   │   └── User.kt                 # User JPA 엔티티
    │   │   │
    │   │   ├── repository/
    │   │   │   ├── TodoRepository.kt       # Todo 데이터 액세스 레이어
    │   │   │   └── UserRepository.kt       # User 데이터 액세스 레이어
    │   │   │
    │   │   └── service/
    │   │       ├── TodoService.kt          # Todo 비즈니스 로직
    │   │       └── UserService.kt          # User 비즈니스 로직
    │   │
    │   └── resources/
    │       ├── application.properties      # 애플리케이션 설정
    │       └── templates/                  # Thymeleaf 템플릿
    │           ├── create.html             # Todo 생성 페이지
    │           ├── list.html               # Todo 목록 페이지
    │           ├── login.html              # 로그인 페이지
    │           └── signup.html             # 회원가입 페이지
    │
    └── test/
        └── kotlin/com/example/todo/
            └── TodoApplicationTests.kt     # 애플리케이션 테스트
```

## 🏗 아키텍처 구조

### 1. **Entity Layer**
- `User.kt`: 사용자 정보 (이메일, 암호화된 비밀번호, 닉네임)
- `Todo.kt`: 할일 정보 (제목, 설명, 완료상태, 사용자 연관관계)

### 2. **Repository Layer**
- `UserRepository.kt`: 사용자 데이터 CRUD 및 이메일 기반 조회
- `TodoRepository.kt`: 할일 데이터 CRUD 및 사용자별 필터링

### 3. **Service Layer**
- `UserService.kt`: 회원가입, 로그인, 비밀번호 암호화 처리
- `TodoService.kt`: 할일 CRUD 비즈니스 로직, 사용자별 데이터 격리

### 4. **Controller Layer**
- `AuthController.kt`: 인증 관련 웹 페이지 처리
- `TodoWebController.kt`: 할일 관련 웹 페이지 처리 (세션 검증 포함)
- `TodoController.kt`: REST API 엔드포인트

### 5. **DTO Layer**
- `TodoRequest/Response.kt`: 할일 데이터 전송 객체
- `UserRequest.kt`: 회원가입/로그인 요청 객체

### 6. **Configuration**
- `SecurityConfig.kt`: Spring Security 설정, BCrypt 암호화, 권한 설정

## 🔐 보안 구조

### 인증 방식
- **세션 기반 인증**: HttpSession을 사용한 사용자 상태 관리
- **비밀번호 암호화**: BCrypt를 사용한 단방향 암호화
- **접근 제어**: 로그인하지 않은 사용자는 자동으로 로그인 페이지로 리다이렉트

### 데이터 격리
- 각 사용자는 자신의 Todo만 접근 가능
- Repository 레벨에서 userId 기반 필터링
- 모든 Todo 작업에서 세션 검증 수행

## 🌐 주요 엔드포인트

### 웹 페이지
- `GET /signup` - 회원가입 폼
- `POST /signup` - 회원가입 처리
- `GET /login` - 로그인 폼
- `POST /login` - 로그인 처리
- `POST /logout` - 로그아웃 처리
- `GET /todos` - Todo 목록 페이지
- `GET /todos/create` - Todo 생성 폼
- `POST /todos/create` - Todo 생성 처리
- `POST /todos/{id}/toggle` - Todo 완료상태 토글
- `POST /todos/{id}/delete` - Todo 삭제

### REST API
- `GET /api/todos` - Todo 목록 조회
- `POST /api/todos` - Todo 생성
- `GET /api/todos/{id}` - Todo 단건 조회
- `PUT /api/todos/{id}` - Todo 수정
- `DELETE /api/todos/{id}` - Todo 삭제

## 🗄 데이터베이스 구조

### users 테이블
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 사용자 ID |
| email | VARCHAR | UNIQUE, NOT NULL | 이메일 (로그인 ID) |
| password | VARCHAR | NOT NULL | BCrypt 암호화된 비밀번호 |
| nickname | VARCHAR | NOT NULL | 사용자 닉네임 |

### todos 테이블
| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | BIGINT | PK, AUTO_INCREMENT | Todo ID |
| title | VARCHAR | NOT NULL | Todo 제목 |
| description | VARCHAR(1000) | NULLABLE | Todo 설명 |
| is_done | BOOLEAN | NOT NULL, DEFAULT FALSE | 완료 여부 |
| user_id | BIGINT | FK, NOT NULL | 작성자 ID |

## 🚀 실행 방법

1. **프로젝트 클론 및 빌드**
   ```bash
   ./gradlew build
   ```

2. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

3. **접속**
   - 웹 애플리케이션: http://localhost:8080
   - H2 콘솔: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:testdb`
     - Username: `sa`
     - Password: `password`

## 📝 주요 특징

- ✅ **사용자별 데이터 격리**: 로그인한 사용자만 자신의 Todo 관리 가능
- ✅ **세션 기반 인증**: 간단하고 직관적인 인증 시스템
- ✅ **반응형 UI**: Thymeleaf 기반의 깔끔한 웹 인터페이스
- ✅ **데이터 검증**: 백엔드 및 프론트엔드 입력값 검증
- ✅ **보안**: BCrypt 암호화, CSRF 보호 비활성화 (개발용)
- ✅ **RESTful API**: 웹 페이지와 별도의 REST API 제공

## by naoek