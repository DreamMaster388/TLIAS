# Sidebar Navigation Design

## Overview

Add a left sidebar navigation to the IEMS employee management system, with three tabs: Employees (existing table), Departments (read-only list from DB), and Statistics (empty placeholder). The top navbar is simplified — search and add buttons move into the employee page.

## Layout

```
+----------------------------------------------------------+
| [IEMS] 通知滚动条                          [退出登录]     |
+----------+-----------------------------------------------+
| SIDEBAR   |  MAIN CONTENT AREA                           |
| 200px     |  (varies by active tab)                      |
| dark bg   |                                              |
|           |  Employees tab: [搜索] [添加员工] + table     |
| 👥 员工   |  Departments tab: ID / 名称 / 创建时间 table  |
| 🏢 部门   |  Statistics tab: "开发中" placeholder        |
| 📊 统计   |                                              |
+----------+-----------------------------------------------+
|  Footer (unchanged)                                      |
+----------------------------------------------------------+
```

## Layout Architecture

- Top navbar: keep IEMS logo, notification ticker, logout button. Remove search and add buttons.
- Sidebar: 200px wide, `#1e3a5f` background matching navbar. Dark theme.
- Main content: fills remaining horizontal space. Conditionally renders one of three page sections.
- Footer: unchanged.

## Frontend — HTML (tlias.html)

Wrap existing content in a new `.app-body` flex container:

```html
<div class="app-body">
  <aside class="sidebar">
    <div class="sidebar-item" @click="activeTab='employees'" :class="{active: activeTab==='employees'}">👥 员工管理</div>
    <div class="sidebar-item" @click="activeTab='departments'" :class="{active: activeTab==='departments'}">🏢 部门管理</div>
    <div class="sidebar-item" @click="activeTab='statistics'" :class="{active: activeTab==='statistics'}">📊 统计分析</div>
  </aside>
  <main class="main-content">
    <section v-if="activeTab==='employees'" class="page-section">
      <!-- employee search bar + table + pagination -->
    </section>
    <section v-if="activeTab==='departments'" class="page-section">
      <!-- department table -->
    </section>
    <section v-if="activeTab==='statistics'" class="page-section">
      <!-- placeholder -->
    </section>
  </main>
</div>
```

## Frontend — Vue State (app.js)

Add to `data()`:
- `activeTab: 'employees'`

No new methods needed for tab switching (inline `@click` handlers).

## Frontend — CSS (style.css)

New styles:
- `.app-body`: flex container, min-height calc(100vh - navbar - footer)
- `.sidebar`: width 200px, background #1e3a5f, padding, flex column
- `.sidebar-item`: padding 14px 20px, color white/translucent, cursor pointer, transition
- `.sidebar-item.active`: background rgba(255,255,255,.12), font-weight 600
- `.sidebar-item:hover`: background rgba(255,255,255,.06)
- `.main-content`: flex 1, padding, overflow auto
- `.page-section`: contains page-specific content

Adjust existing styles:
- `.table-section`: reduce padding (currently 0 40px) since main-content provides spacing
- `.navbar`: remove search/add buttons (keep only h1, notify, logout)

## Backend — Department Entity (Department.java)

Add field:
```java
private LocalDateTime createdAt;
```

MyBatis `map-underscore-to-camel-case: true` auto-maps `created_at` → `createdAt`.

## Backend — Department Mapper (DepartmentMapper.xml)

No change needed. Current query `SELECT * FROM departments` already includes `created_at`. The new `createdAt` field on the entity is auto-mapped by MyBatis camelCase config.

## Pages Detail

### Employees Page
- Page title: "员工管理"
- Action bar: [搜索] [添加员工] buttons (previously in navbar)
- Employee table (existing, unchanged)
- Pagination (existing, unchanged)

### Departments Page
- Page title: "部门管理"
- Table columns: ID | 部门名称 | 创建时间
- Read-only (no add/edit/delete)
- Data fetched from `GET /api/departments`

### Statistics Page
- Page title: "数据分析"
- Placeholder: "统计数据功能开发中..."

## File Change Summary

| File | Type | Changes |
|------|------|---------|
| `Department.java` | Java entity | Add `createdAt` field |
| `DepartmentMapper.xml` | MyBatis XML | No change (already `SELECT *` includes created_at) |
| `tlias.html` | HTML | Add sidebar, restructure layout, move buttons, add department/statistics sections |
| `style.css` | CSS | Add sidebar styles, adjust existing layout |
| `app.js` | Vue JS | Add `activeTab`, department data handling, statistics placeholder |
