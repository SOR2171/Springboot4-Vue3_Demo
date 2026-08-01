# API 文档

Base URL：`http://localhost:8080/api`

## 通用约定

所有接口均返回统一 JSON 结构：

```typescript
class Response<T> {
    "code": number;
    "message": string;
    "data": T | null;
}
```

- `code === 200` 表示成功；**`code !== 200` 时 `data` 恒为 `null`**
- 除特别标注（公开）外，接口需在请求头携带 `Authorization: Bearer <token>`
- 下文路径均省略 `/api` 前缀，`data` 仅给出 `T` 的声明
- 部分 `/test` 接口的返回值不遵循上述格式

## 接口总览

| 分组 | 路径前缀   | 说明                              |
|------|------------|-----------------------------------|
| 测试 | `/test`    | 连通性测试                        |
| 认证 | `/v1/auth` | 登录 / 注册 / 验证码 / Token 管理 |

---

## 分组：测试 `/test`

### GET /hello

公开接口。

**响应**

```typescript
"Hello Kotlin WebFlux!"
```

---

## 分组：认证 `/v1/auth`

| 方法   | 路径                      | 认证 | 说明                     |
|--------|---------------------------|------|--------------------------|
| POST   | `/login`                  | 公开 | 登录，获取 Token         |
| GET    | `/logout`                 | 需要 | 注销，Token 加入黑名单   |
| GET    | `/ask-code`               | 公开 | 发送邮箱验证码           |
| POST   | `/register`               | 公开 | 邮箱注册                 |
| POST   | `/reset`                  | 公开 | 重置密码                 |
| GET    | `/relogin`                | 需要 | 用旧 Token 换取新 Token  |

### POST /login

**请求参数**

| 字段     | 类型   | 说明     |
|----------|--------|----------|
| username | string | 用户名   |
| password | string | 密码     |

**响应 data**

```typescript
class data {
    "username": string;
    "role": string;
    "token": string;
    "expire": Date;
}
```

### GET /logout

**请求参数**：无

**响应 data**：`null`

### GET /ask-code

**请求参数**

| 字段  | 类型                        | 说明                               |
|-------|-----------------------------|------------------------------------|
| email | string                      | 接收验证码的邮箱                   |
| type  | `register` \| `reset`       | 验证码用途（注册 / 重置密码）      |

**响应 data**：`null`

### POST /register

**请求体字段**：`email`、`code`、`username`、`password`

**响应 data**：`null`

### POST /reset

**请求体字段**：`email`、`code`、`password`

**响应 data**：`null`

### GET /relogin

**请求参数**：无

**响应 data**

```typescript
class data {
    "token": string;
    "expire": time;
}
```

---

## 其他

### WebSocket

`ws://localhost:8080/ws` — 目前不支持
