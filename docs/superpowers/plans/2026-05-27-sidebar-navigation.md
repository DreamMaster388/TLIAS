# Sidebar Navigation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a left sidebar navigation with three tabs (Employees, Departments, Statistics) and restructure the layout.

**Architecture:** Dark sidebar (200px, `#1e3a5f`) on the left, conditional page sections on the right. Top navbar simplified — search/add buttons move into the employee page. Department entity gets `createdAt` field for the read-only department table.

**Tech Stack:** Spring Boot 4.0.6 + MyBatis + Vue 3 (CDN) + vanilla CSS

---

### Task 1: Add `createdAt` field to Department entity

**Files:** Modify `src/main/java/com/example/day04/entity/Department.java`

- [ ] **Step 1: Add `createdAt` field**

```java
package com.example.day04.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Department {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
```

- [ ] **Step 2: Verify tests still pass**

Run: `./mvnw.cmd test`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/example/day04/entity/Department.java
git commit -m "feat: add createdAt field to Department entity"
```

---

### Task 2: Restructure HTML — add sidebar and page sections

**Files:** Modify `src/main/resources/static/tlias/tlias.html`

- [ ] **Step 1: Restructure the layout**

Wrap the navbar content to remove search/add buttons, then add the sidebar + main-content flex layout.

Current navbar has:
```html
<button class="btn-search-nav" @click="openSearchModal">搜索</button>
<button class="btn-add-nav" @click="openAddModal">添加员工</button>
<button class="logout">退出登录</button>
```

Change to:
```html
<button class="logout">退出登录</button>
```

After the navbar `</div>` and before the `.table-section`, insert the sidebar + main-content wrapper:

```html
<div class="app-body">
  <aside class="sidebar">
    <div class="sidebar-item" :class="{ active: activeTab === 'employees' }" @click="activeTab = 'employees'">
      <span class="sidebar-icon">👥</span> 员工管理
    </div>
    <div class="sidebar-item" :class="{ active: activeTab === 'departments' }" @click="activeTab = 'departments'">
      <span class="sidebar-icon">🏢</span> 部门管理
    </div>
    <div class="sidebar-item" :class="{ active: activeTab === 'statistics' }" @click="activeTab = 'statistics'">
      <span class="sidebar-icon">📊</span> 统计分析
    </div>
  </aside>
  <main class="main-content">
```

- [ ] **Step 2: Wrap employee content in employees page section**

Replace the opening `.table-section` with:

```html
    <section class="page-section" v-if="activeTab === 'employees'">
      <div class="page-header">
        <h2>员工管理</h2>
        <div class="page-actions">
          <button class="btn-search-nav" @click="openSearchModal">搜索</button>
          <button class="btn-add-nav" @click="openAddModal">添加员工</button>
        </div>
      </div>
      <div class="table-section">
        ...existing table content...
      </div>
    </section>
```

And close the employee section before the footer:

```html
  </main>
</div>
```

- [ ] **Step 3: Add departments page section**

After the employee `</section>` and before `</main>`:

```html
    <section class="page-section" v-if="activeTab === 'departments'">
      <div class="page-header">
        <h2>部门管理</h2>
      </div>
      <div class="table-section">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>部门名称</th>
              <th>创建时间</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="departments.length === 0">
              <td colspan="3" class="empty-hint">暂无部门信息</td>
            </tr>
            <tr v-for="d in departments" :key="d.id">
              <td>{{ d.id }}</td>
              <td>{{ d.name }}</td>
              <td>{{ d.createdAt }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
```

- [ ] **Step 4: Add statistics page section**

After the departments `</section>`:

```html
    <section class="page-section" v-if="activeTab === 'statistics'">
      <div class="page-header">
        <h2>数据分析</h2>
      </div>
      <div class="placeholder-content">
        <p>统计数据功能开发中...</p>
      </div>
    </section>
```

- [ ] **Step 5: Verify HTML is well-formed**

Read the file and confirm all tags close properly, and the `</main></div>` wrapper is before the footer div.

- [ ] **Step 6: Commit**

```bash
git add src/main/resources/static/tlias/tlias.html
git commit -m "feat: add sidebar navigation and page sections to HTML"
```

---

### Task 3: Add CSS styles for sidebar and layout

**Files:** Modify `src/main/resources/static/tlias/css/style.css`

- [ ] **Step 1: Add `.app-body` flex container**

After the navbar styles (around line 99), add:

```css
.app-body {
    display: flex;
    min-height: calc(100vh - 60px - 110px);
}
```

- [ ] **Step 2: Add sidebar styles**

```css
.sidebar {
    width: 200px;
    min-width: 200px;
    background: #1e3a5f;
    padding: 16px 0;
    display: flex;
    flex-direction: column;
}
.sidebar-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 14px 20px;
    color: rgba(255,255,255,.65);
    font-size: .95rem;
    cursor: pointer;
    transition: all .2s;
    user-select: none;
}
.sidebar-item:hover {
    background: rgba(255,255,255,.06);
    color: rgba(255,255,255,.9);
}
.sidebar-item.active {
    background: rgba(255,255,255,.12);
    color: #fff;
    font-weight: 600;
}
.sidebar-icon {
    font-size: 1.1rem;
    width: 24px;
    text-align: center;
}
```

- [ ] **Step 3: Add main content styles**

```css
.main-content {
    flex: 1;
    padding: 24px 32px;
    overflow: auto;
    background: #f0f2f5;
}
.page-section {
    animation: fadeIn .3s ease;
}
@keyframes fadeIn {
    from { opacity: 0; transform: translateY(6px); }
    to { opacity: 1; transform: translateY(0); }
}
.page-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
}
.page-header h2 {
    font-size: 1.2rem;
    color: #1e3a5f;
    font-weight: 600;
}
.page-actions {
    display: flex;
    gap: 10px;
}
.placeholder-content {
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 300px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 6px rgba(0,0,0,.06);
    color: #999;
    font-size: 1rem;
}
```

- [ ] **Step 4: Adjust `.table-section` padding**

Change the existing `.table-section` padding from `0 40px` to `0` (main-content provides the spacing):

```css
.table-section {
    padding: 0;
}
```

- [ ] **Step 5: Adjust `.navbar` — remove old nav button styles that are no longer used**

The `.btn-search-nav` and `.btn-add-nav` styles are still needed (they're now used in `.page-actions`), so keep them. Just delete the unused search/add button handles from the navbar HTML.

- [ ] **Step 6: Commit**

```bash
git add src/main/resources/static/tlias/css/style.css
git commit -m "feat: add sidebar and layout CSS styles"
```

---

### Task 4: Add Vue activeTab state and department data handling

**Files:** Modify `src/main/resources/static/tlias/js/app.js`

- [ ] **Step 1: Add `activeTab` to data**

```javascript
data() {
    return {
        activeTab: 'employees',
        // ... existing fields
    }
}
```

- [ ] **Step 2: Add `formatDate` helper method for department time display**

Add this method to format the `createdAt` timestamp into a readable date string:

```javascript
methods: {
    // ... existing methods ...
    formatDate(dt) {
        if (!dt) return '';
        return dt.replace('T', ' ').substring(0, 19);
    },
}
```

- [ ] **Step 3: Update HTML to use `formatDate`**

In tlias.html, change the department table cell from `{{ d.createdAt }}` to `{{ formatDate(d.createdAt) }}`.

- [ ] **Step 4: Ensure departments data is loaded on mount**

The `fetchDepartments()` is already called in `mounted()`. No changes needed — data is available for the departments tab.

- [ ] **Step 5: Commit**

```bash
git add src/main/resources/static/tlias/js/app.js
git commit -m "feat: add activeTab state for sidebar navigation"
```

---

### Task 5: Run tests and verify

- [ ] **Step 1: Run all tests**

Run: `./mvnw.cmd test`
Expected: BUILD SUCCESS, all tests pass

- [ ] **Step 2: Final commit**

```bash
git add -A
git commit -m "feat: implement sidebar navigation with employee/department/statistics tabs"
```
