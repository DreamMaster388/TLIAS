## PR: 部门/职位表结构重构 — 关联表 + 级联下拉

### 变更背景

`employee` 表的 `department` / `position` 字段已迁移至关联表：

- `departments` — 部门表（id, name）
- `department_positions` — 部门职位关系表（id, department_id, position_name）
- `employee.dept_pos_id` → 引用 `department_positions.id`

### 后端改动

| 文件 | 说明 |
|---|---|
| `entity/Department.java` | 新增，部门实体 |
| `entity/DepartmentPosition.java` | 新增，部门职位实体 |
| `mapper/DepartmentMapper.java` + `.xml` | 新增，查询所有部门 |
| `mapper/DepartmentPositionMapper.java` + `.xml` | 新增，按部门查询职位 |
| `controller/DepartmentController.java` | 新增，提供下拉选项 API |
| `entity/Employee.java` | 加 `deptPosId` 字段 |
| `EmployeeMapper.xml` | SELECT 改用 LEFT JOIN 获取名称；INSERT/UPDATE 改用 `dept_pos_id` |

### 前端改动

| 文件 | 说明 |
|---|---|
| `js/app.js` | 页面加载时拉取部门列表；添加/编辑表单的部门/职位改为级联下拉选择 |
| `tlias.html` | 添加/编辑模态框中部门/职位输入改为 `<select>` 下拉框 |
| `tlias.html`（搜索模态框） | 保留文本输入，后端 JOIN 模糊搜索正常工作 |

### API 新增

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/departments` | 获取所有部门 |
| GET | `/api/departments/{id}/positions` | 获取某部门下所有职位 |

### 测试

```
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
```
