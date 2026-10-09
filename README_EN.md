# Springboot4-Vue3

> A modern full-stack starter template / demo project based on **Spring Boot 4 (Kotlin + WebFlux)** and **Vue 3 (Vite +
TypeScript + Element Plus)**.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.5-brightgreen.svg)](https://vuejs.org/)
[![Vite](https://img.shields.io/badge/Vite-7.2-purple.svg)](https://vitejs.dev/)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-blue.svg)](https://kotlinlang.org/)
[![Java](https://img.shields.io/badge/JDK-25+-orange.svg)](https://openjdk.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.9-blue.svg)](https://www.typescriptlang.org/)

[![License](https://img.shields.io/badge/license-MIT-green.svg)](./LICENSE)

###### ~~*Long may the Student Pack*~~

[简体中文](./README.md) / English

---

## Introduction

This project is a modern boilerplate for front-end and back-end separation built with cutting-edge technologies. The
backend uses **Spring Boot 4 + Kotlin** with a fully asynchronous reactive architecture (Spring WebFlux) and coroutines,
integrated with Spring Security and JWT for authentication. The frontend is powered by **Vue 3 + Vite 7 + TypeScript +
Element Plus**, providing out-of-the-box pages for login, registration, password recovery, and the home dashboard.

### Features

- **Modern Tech Stack**: Spring Boot 4.0, Kotlin 2.3, and JDK 25, embracing the latest language and framework
  improvements.
- **Reactive & Coroutines**: Built on Spring WebFlux + Reactor + Kotlin Coroutines for high throughput and low resource
  footprint.
- **Authentication & Security**: Spring Security + JWT stateless authentication, token renewal (relogin), token
  blacklisting on logout, and global CORS configuration.
- **Rate Limiting**: Reactive Redis-backed sliding window rate limiter and IP/user ban controls (Flow Limit).
- **Asynchronous Queue**: RabbitMQ + Spring Mail for decoupled async email verification (user registration & password
  reset).
- **Real-time Communication**: Reactive WebSocket handshake and messaging support.
- **Engineered Frontend**: Vue 3 (Composition API `<script setup>`) + Vite 7 + TypeScript, on-demand component
  auto-importing, Axios request/response interceptors, and route guards.

---

## 🛠️ Tech Stack

### Backend

| Component / Dependency       | Technology                            | Notes                                  |
|:-----------------------------|:--------------------------------------|:---------------------------------------|
| **Language**                 | Kotlin `2.3.20`                       | JVM 25 target                          |
| **Framework**                | Spring Boot `4.0.4`                   | Core starter                           |
| **Web Layer**                | Spring WebFlux                        | Reactive non-blocking web framework    |
| **Concurrency**              | Kotlin Coroutines + Reactor           | Coroutines & Reactive streams          |
| **Security**                 | Spring Security + `java-jwt`          | Stateless JWT authentication & filters |
| **Persistence**              | MyBatis-Plus `3.5.15` + MySQL 8.0+    | ORM & Relational database              |
| **Caching / Reactive Store** | Spring Data Redis (Reactive)          | Caching & rate limiting counter        |
| **Message Queue**            | RabbitMQ (`spring-boot-starter-amqp`) | Decoupled async email queue            |
| **Mail Service**             | Spring Boot Starter Mail              | Verification code delivery             |
| **Build Tool**               | Gradle (`build.gradle.kts`)           | Kotlin DSL with Gradle Wrapper         |

### Frontend

| Component / Dependency | Technology                             | Notes                                        |
|:-----------------------|:---------------------------------------|:---------------------------------------------|
| **Framework**          | Vue `3.5`                              | Composition API (`<script setup>`)           |
| **Build Tool**         | Vite `7.2`                             | Fast HMR & bundling                          |
| **Language**           | TypeScript `5.9`                       | Type safety                                  |
| **UI Library**         | Element Plus `2.11`                    | Desktop UI with auto-import support          |
| **Routing**            | Vue Router `4.6`                       | SPA routing & navigation guards              |
| **HTTP Client**        | Axios `1.13`                           | Unified interceptors & token injection       |
| **Utilities**          | VueUse, marked, markdown-it, DOMPurify | Utility hooks & sanitized Markdown rendering |

---

## Requirements

Ensure the following environments are installed before running the project:

- **JDK**: `25` or higher
- **Kotlin**: `2.3.20` or higher
- **Node.js**: `18.0.0` or higher (Node 20 LTS recommended)
- **MySQL**: `8.0` or higher
- **Redis**: `6.0` or higher
- **RabbitMQ**: `3.8` or higher

---

## Quick Start

### 1. Clone Repository

```bash
git clone https://github.com/SOR2171/Springboot4-Vue3_Demo.git
cd Springboot4-Vue3_Demo
```

---

### 2. Backend Setup & Run

#### Step 1: Database Initialization

1. Create a MySQL database named `kotlin_springboot` (recommended charset: `utf8mb4`).
2. Import the initial SQL script: [`backend/src/main/resources/springboot_vue3.sql`](./backend/src/main/resources/springboot_vue3.sql).

> **Default Test User**:
> - Username: `test`
> - Password: `123456`
> - *Note: The SQL script contains a pre-hashed BCrypt password. You can run unit tests in `BackendApplicationTests` to
    generate new hashes or reset the user.*

#### Step 2: Configure Properties

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Create `application.yaml` based on the example:
   Copy [`src/main/resources/application-example.yaml`](./backend/src/main/resources/application-example.yaml) to
   `src/main/resources/application.yaml`, and update with your actual credentials:
    - MySQL datasource (`spring.datasource`)
    - Redis host & port
    - RabbitMQ connection (`spring.rabbitmq`)
    - Mail SMTP credentials (`spring.mail`)
    - JWT secret key (`spring.security.jwt.key`)

#### Step 3: Build & Start

- **Development Mode**:
  ```bash
  # Linux / macOS
  ./gradlew bootRun

  # Windows
  .\gradlew.bat bootRun
  ```

- **Build Executable JAR**:
  ```bash
  # Linux / macOS
  ./gradlew build -x test

  # Windows
  .\gradlew.bat build -x test
  ```
  The packaged JAR will be located at `build/libs/backend-1.0.0.jar`.

- **Run JAR**:
  ```bash
  java --enable-native-access=ALL-UNNAMED -jar build/libs/backend-1.0.0.jar
  ```

---

### 3. Frontend Setup & Run

#### Step 1: Install Dependencies

```bash
cd frontend
npm install
```

#### Step 2: Development Server

```bash
npm run dev
```

Open the displayed URL in your browser (default: `http://localhost:5173`).

> **Background Image**:
> The welcome & login background image is located at [`frontend/src/assets/welcome-image.png`](./frontend/src/assets/welcome-image.png). A default image is already provided;
> replace it with your own image if desired.

#### Step 3: Production Build

```bash
npm run build
```

The compiled static assets will be output to the `frontend/dist` directory.

---

## Nginx Deployment (Optional)

For production deployment with Nginx hosting the frontend and reverse proxying backend APIs:

```nginx
server {
    listen       80;
    server_name  localhost; # Replace with your domain or IP

    # Frontend SPA static files
    location / {
        root   /path/to/Springboot4-Vue3/frontend/dist;
        index  index.html;
        # Support Vue Router history mode
        try_files $uri $uri/ /index.html;
    }

    # Backend RESTful API reverse proxy
    location /api/ {
        proxy_pass         http://127.0.0.1:8080/api/;
        proxy_set_header   Host $host;
        proxy_set_header   X-Real-IP $remote_addr;
        proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto $scheme;
    }

    # WebSocket proxy (if needed)
    location /ws/ {
        proxy_pass         http://127.0.0.1:8080/ws/;
        proxy_http_version 1.1;
        proxy_set_header   Upgrade $http_upgrade;
        proxy_set_header   Connection "upgrade";
        proxy_set_header   Host $host;
    }
}
```

Validate and reload Nginx:

```bash
sudo nginx -t
sudo systemctl reload nginx
```

---

## Project Structure

```text
Springboot4-Vue3/
├── backend/                       # Backend project (Kotlin + Spring Boot 4)
│   ├── src/main/kotlin/           # Kotlin source code
│   │   └── com/github/sor2171/backend/
│   │       ├── config/            # Security, RabbitMQ, Serialization, WebSocket config
│   │       ├── controller/        # RESTful API controllers
│   │       ├── entity/            # Entity, DTO, VO, and enum definitions
│   │       ├── filter/            # CORS, Flow limit, and JWT authentication filters
│   │       ├── listener/          # RabbitMQ message listeners (async email, etc.)
│   │       ├── mapper/            # MyBatis-Plus database mappers
│   │       ├── service/           # Business service interfaces and implementations
│   │       └── utils/             # Utilities (JWT, flow limit, date, etc.)
│   ├── src/main/resources/        # Config files and database SQL init script
│   ├── api.md                     # Detailed backend API documentation
│   └── build.gradle.kts           # Gradle dependencies and build script
├── frontend/                      # Frontend project (Vue 3 + Vite + TypeScript)
│   ├── src/
│   │   ├── api/                   # Axios request wrappers & API services
│   │   ├── assets/                # Static assets (including welcome image)
│   │   ├── interfaces/            # TypeScript interfaces & types
│   │   ├── router/                # Route definitions & guards
│   │   ├── utils/                 # JWT storage & local utilities
│   │   └── views/                 # Views (Welcome, Login, Register, Reset, Home)
│   ├── package.json               # Dependencies and scripts
│   └── vite.config.ts             # Vite build and plugin configurations
├── LICENSE                        # Open-source license (MIT)
├── README.md                      # Chinese documentation (Default)
└── README_EN.md                   # English documentation
```

---

## Documentation

- Detailed API specifications can be found in: [Backend API Documentation](./backend/api.md)

---

## License

This project is licensed under the [MIT License](./LICENSE).
