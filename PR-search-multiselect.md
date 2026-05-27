## PR: 搜索框部门/职位改为多选下拉 + 标签式交互

### 变更背景

将搜索框中部门、职位的自由文本输入改为多选下拉选择 + 灰色标签交互，提升搜索精确度与用户体验。

### 交互方式

- **部门**：点击下拉 → 选择一个 → 下拉关闭 → 生成灰色标签；再次点击可继续选择
- **职位**：仅当已选至少一个部门时出现，选项来自已选部门的职位合并；选中同样生成标签
- **标签**：每个已选项显示为灰色标签 + × 按钮，点击 × 移除
- **清空**：清空部门时同步清空职位

### 后端改动

| 文件 | 改动 |
|---|---|
| `EmployeeSearchCriteria.java` | `department`/`position` 改为 `departmentIds`/`positionNames` 列表 |
| `DepartmentPositionMapper.java` | 新增 `findByDepartmentIds(List<Long>)` 批量查询 |
| `DepartmentPositionMapper.xml` | 新增 `<foreach>` 批量查询 SQL |
| `DepartmentController.java` | 新增 `POST /api/departments/positions-by-ids` 端点 |
| `EmployeeMapper.xml` | 搜索 WHERE 从句改为 `d.id IN (...)` + `dp.position_name IN (...)` |

### 前端改动

| 文件 | 改动 |
|---|---|
| `css/style.css` | 新增 `.search-dropdown`、`.tag-group`、`.tag-item` 等 77 行样式 |
| `js/app.js` | 新增 7 个搜索方法（展开/选择/移除/加载职位）；重写 `clear()`/`fetchEmployees()` |
| `tlias.html` | 搜索模态框中部门/职位改为下拉 + 标签 UI |

### 新增 API

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/departments/positions-by-ids` | 传入部门 ID 列表，返回合并后的职位列表 |

### 测试

```
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
```

### 变更统计

```
9 files changed, 207 insertions(+), 20 deletions(-)
```
