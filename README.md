# Springboot4-Vue3

> 基于 **Spring Boot 4 (Kotlin + WebFlux)** 与 **Vue 3 (Vite + TypeScript + Element Plus)**
> 的现代化前后端分离全栈脚手架 / 示例项目。

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.5-brightgreen.svg)](https://vuejs.org/)
[![Vite](https://img.shields.io/badge/Vite-7.2-purple.svg)](https://vitejs.dev/)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-blue.svg)](https://kotlinlang.org/)
[![Java](https://img.shields.io/badge/JDK-25+-orange.svg)](https://openjdk.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.9-blue.svg)](https://www.typescriptlang.org/)

[![License](https://img.shields.io/badge/license-MIT-green.svg)](./LICENSE)

###### ~~*赞美学生包*~~

简体中文 / [English](./README_EN.md)

---

## 项目简介

本项目是一套采用前沿技术栈构建的现代化前后端分离基础模板。后端基于 **Spring Boot 4 + Kotlin**，采用全异步响应式（Reactive
WebFlux）与协程架构，结合 Spring Security 与 JWT 实现了完备的安全认证；前端采用 **Vue 3 + Vite 7 + TypeScript + Element
Plus**，提供了开箱即用的登录、注册、找回密码与主页框架。

### 核心特性

- **前沿技术选型**：基于 Spring Boot 4.0、Kotlin 2.3 与 JDK 25，拥抱最新的现代化语言与框架特性。
- **响应式与协程**：基于 Spring WebFlux + Reactor + Kotlin Coroutines，高并发、低资源占用。
- **认证与安全**：集成 Spring Security + JWT，支持无状态鉴权、Token 自动刷新（Relogin）、注销黑名单机制与全局 CORS 跨域处理。
- **接口限流防护**：基于 Redis Reactive 实现滑动窗口限流与频次控制（Flow Limit），防止接口被恶意刷取。
- **异步消息解耦**：集成 RabbitMQ + Spring Mail，实现邮件验证码异步发送（支持用户注册与密码重置）。
- **实时通讯支持**：集成响应式 WebSocket 握手与消息通信。
- **工程化前端**：Vue 3 (Composition API `<script setup>`) + Vite 7 + TypeScript，结合 Element Plus 自动按需导入、统一
  Axios 请求拦截与路由鉴权守卫。

---

## 🛠️ 技术栈

### 后端 (Backend)

| 组件 / 依赖           | 技术选型                              | 说明                              |
|:----------------------|:--------------------------------------|:----------------------------------|
| **开发语言**          | Kotlin `2.3.20`                       | 基于 JVM 25 运行环境              |
| **基础框架**          | Spring Boot `4.0.4`                   | 核心脚手架                        |
| **Web 架构**          | Spring WebFlux                        | 响应式非阻塞 Web 框架             |
| **异步方案**          | Kotlin Coroutines + Reactor           | 协程与响应式流                    |
| **安全框架**          | Spring Security + `java-jwt`          | 权限控制与无状态 JWT 鉴权         |
| **持久层**            | MyBatis-Plus `3.5.15` + MySQL 8.0+    | ORM 框架与关系型数据库            |
| **缓存 / 响应式存储** | Spring Data Redis (Reactive)          | 响应式 Redis 缓存与限流计数器     |
| **消息队列**          | RabbitMQ (`spring-boot-starter-amqp`) | 异步邮件发送解耦                  |
| **邮件服务**          | Spring Boot Starter Mail              | 验证码邮件发送                    |
| **构建工具**          | Gradle (`build.gradle.kts`)           | Kotlin DSL 构建脚本，内置 Wrapper |

### 前端 (Frontend)

| 组件 / 依赖   | 技术选型                               | 说明                                  |
|:--------------|:---------------------------------------|:--------------------------------------|
| **核心框架**  | Vue `3.5`                              | Composition API (`<script setup>`)    |
| **构建工具**  | Vite `7.2`                             | 极速冷启动与热重载                    |
| **开发语言**  | TypeScript `5.9`                       | 强类型支持                            |
| **UI 组件库** | Element Plus `2.11`                    | 桌面端组件库，支持自动按需导入        |
| **路由管理**  | Vue Router `4.6`                       | 单页应用路由管理与导航守卫            |
| **网络请求**  | Axios `1.13`                           | 统一封装拦截器、响应结构与 Token 传递 |
| **常用工具**  | VueUse, marked, markdown-it, DOMPurify | 组合式工具集与 Markdown 安全渲染      |

---

## 环境要求

在部署与运行项目之前，请确保本地已安装并配置好以下基础环境：

- **JDK**：`25` 及以上
- **Kotlin**：`2.3.20` 及以上
- **Node.js**：`18.0.0` 或更高版本（推荐 Node 20 LTS）
- **MySQL**：`8.0` 及以上
- **Redis**：`6.0` 及以上
- **RabbitMQ**：`3.8` 及以上

---

## 快速开始

### 1. 克隆仓库

```bash
git clone https://github.com/SOR2171/Springboot4-Vue3_Demo.git
cd Springboot4-Vue3_Demo
```

---

### 2. 后端配置与运行

#### 步骤 1：导入数据库

1. 在 MySQL 中创建名为 `kotlin_springboot` 的数据库（字符编码建议为 `utf8mb4`）。
2. 导入预置 SQL 脚本：[`backend/src/main/resources/springboot_vue3.sql`](./backend/src/main/resources/springboot_vue3.sql)。

> **预置默认用户**：
> - 用户名：`test`
> - 密码：`123456`
> - *说明：SQL 脚本中已包含该用户的 BCrypt 密文哈希。如果需要重置测试用户或重新生成密文，可运行单元测试类
    `BackendApplicationTests`。*

#### 步骤 2：配置应用参数

1. 进入后端目录：
   ```bash
   cd backend
   ```
2. 参考模板创建配置文件：
   将 [`src/main/resources/application-example.yaml`](./backend/src/main/resources/application-example.yaml) 复制为
   `src/main/resources/application.yaml`，并填写你的本地实际配置：
    - 数据库连接与账号密码 (`spring.datasource`)
    - Redis 连接信息
    - RabbitMQ 连接信息 (`spring.rabbitmq`)
    - 邮件 SMTP 服务配置 (`spring.mail`)
    - JWT 签名密钥 (`spring.security.jwt.key`)

#### 步骤 3：编译与运行

- **本地开发直接运行**：
  ```bash
  # Linux / macOS
  ./gradlew bootRun

  # Windows
  .\gradlew.bat bootRun
  ```

- **打包为 JAR 文件**：
  ```bash
  # Linux / macOS
  ./gradlew build -x test

  # Windows
  .\gradlew.bat build -x test
  ```
  打包生成的 JAR 文件位于：`build/libs/backend-1.0.0.jar`。

- **运行 JAR 包**：
  ```bash
  java --enable-native-access=ALL-UNNAMED -jar build/libs/backend-1.0.0.jar
  ```

---

### 3. 前端配置与运行

#### 步骤 1：安装依赖

```bash
cd frontend
npm install
```

#### 步骤 2：本地开发运行

```bash
npm run dev
```

启动成功后，浏览器访问控制台提示地址（默认：`http://localhost:5173`）。

> **背景图片说明**：
> 欢迎页与登录界面的背景图位于 [`frontend/src/assets/welcome-image.png`](./frontend/src/assets/welcome-image.png)
> 。项目中已内置默认图片，如有需要可直接替换为你喜欢的图片。

#### 步骤 3：生产环境打包

```bash
npm run build
```

打包完成后，生成的静态文件将位于 `frontend/dist` 目录。

---

## Nginx 部署示例（可选）

若在生产环境使用 Nginx 托管前端并代理后端接口，可参考以下配置片段：

```nginx
server {
    listen       80;
    server_name  localhost; # 替换为你的域名或服务器 IP

    # 前端静态资源托管
    location / {
        root   /path/to/Springboot4-Vue3/frontend/dist;
        index  index.html;
        # 支持 Vue Router 的 History 模式
        try_files $uri $uri/ /index.html;
    }

    # 后端 RESTful API 代理
    location /api/ {
        proxy_pass         http://127.0.0.1:8080/api/;
        proxy_set_header   Host $host;
        proxy_set_header   X-Real-IP $remote_addr;
        proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto $scheme;
    }

    # WebSocket 代理（如需使用）
    location /ws/ {
        proxy_pass         http://127.0.0.1:8080/ws/;
        proxy_http_version 1.1;
        proxy_set_header   Upgrade $http_upgrade;
        proxy_set_header   Connection "upgrade";
        proxy_set_header   Host $host;
    }
}
```

配置完成后检查并重新加载 Nginx：

```bash
sudo nginx -t
sudo systemctl reload nginx
```

---

## 项目结构

```text
Springboot4-Vue3/
├── backend/                       # 后端项目 (Kotlin + Spring Boot 4)
│   ├── src/main/kotlin/           # Kotlin 源码
│   │   └── com/github/sor2171/backend/
│   │       ├── config/            # 安全、RabbitMQ、序列化与 WebSocket 配置
│   │       ├── controller/        # RESTful API 控制器
│   │       ├── entity/            # 实体类、DTO、VO 与枚举定义
│   │       ├── filter/            # 跨域 (CORS)、限流与 JWT 认证过滤器
│   │       ├── listener/          # RabbitMQ 消息监听器 (异步邮件等)
│   │       ├── mapper/            # MyBatis-Plus 数据访问接口
│   │       ├── service/           # 业务逻辑接口及实现
│   │       └── utils/             # JWT、限流、日期等通用工具类
│   ├── src/main/resources/        # 配置文件与 SQL 初始化脚本
│   ├── api.md                     # 详细的后端 API 接口文档
│   └── build.gradle.kts           # Gradle 依赖与构建配置
├── frontend/                      # 前端项目 (Vue 3 + Vite + TypeScript)
│   ├── src/
│   │   ├── api/                   # Axios 请求封装与接口统一管理
│   │   ├── assets/                # 静态静态资源 (包含登录背景图)
│   │   ├── interfaces/            # TypeScript 接口与类型定义
│   │   ├── router/                # 路由配置与全局导航守卫
│   │   ├── utils/                 # JWT 存储与本地工具类
│   │   └── views/                 # 页面视图 (欢迎、登录、注册、找回密码、主页)
│   ├── package.json               # 前端项目依赖与脚本
│   └── vite.config.ts             # Vite 构建与插件配置
├── LICENSE                        # 开源协议 (MIT)
├── README.md                      # 中文主说明文档
└── README_EN.md                   # 英文说明文档
```

---

## 详细文档

- 接口设计与调用说明详见：[后端 API 接口文档](./backend/api.md)

---

## 开源许可

本项目基于 [MIT License](./LICENSE) 协议开源。
