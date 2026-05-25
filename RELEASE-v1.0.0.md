## v1.0.0 Release Notes

**Tlias 智能学习辅助系统 — 员工管理模块**

---

### 概述

v1.0.0 是项目首个正式版本，实现了员工信息的完整 CRUD + 搜索分页功能，前后端分离架构，统一 API 返回格式，并配备基础单元测试。

---

### 功能特性

- **员工列表** — 分页展示，每页 10 条，支持首页/上一页/页码跳转/下一页/尾页导航
- **模糊搜索** — 按姓名、部门、职位模糊匹配，按性别精确筛选
- **添加员工** — 模态框表单录入姓名、性别、部门、职位、入职日期
- **编辑员工** — 点击编辑按钮，预填数据后修改保存
- **删除员工** — 确认弹窗后执行删除
- **空结果提示** — 搜索无匹配数据时表格内居中显示提示文字

---

### 技术栈

| 组件 | 版本 |
|---|---|
| Spring Boot | 4.0.6 |
| Java | 17 |
| MyBatis | 3.5.19 (mybatis-spring-boot-starter 4.0.1) |
| MySQL | 8.0.45 |
| 前端 | Vue 3 (CDN) + 原生 CSS |

### 项目架构

```
Controller → Service → Mapper → MySQL
    ↓
Result<T> 统一响应 {code, message, data}
    ↓
GlobalExceptionHandler 异常兜底
```

- 后端三层架构（Controller / Service / Mapper）
- 所有 API 返回统一格式 `Result<T>`
- 全局异常处理，返回 `{code: 0, message: "错误信息"}`

---

### API 接口

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/employees` | 分页查询 + 多条件搜索 |
| POST | `/api/employees` | 添加员工 |
| PUT | `/api/employees/{id}` | 编辑员工 |
| DELETE | `/api/employees/{id}` | 删除员工 |

---

### 测试

11 个测试用例全部通过：

- `ResultTest` (3) — 统一返回格式工厂方法
- `EmployeeControllerTest` (6) — 搜索/增/删/改业务逻辑
- `GlobalExceptionHandlerTest` (1) — 全局异常处理
- `Day04ApplicationTests` (1) — Spring 上下文加载

---

### 启动方式

```powershell
git checkout release/v1.0.0
./mvnw.cmd spring-boot:run
```

浏览器访问 `http://localhost:8080/hello`
