# Search Multi-Select Department/Position Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace free-text department/position search with multi-select dropdown + tag UI

**Architecture:** Backend changes `EmployeeSearchCriteria` from `String department/position` to `List<Long> departmentIds` / `List<String> positionNames`; SQL uses `IN (...)` instead of `LIKE`. Frontend uses custom div-based dropdowns (click-to-open → select → close → generate tag). Department selection triggers position options via batch API.

**Tech Stack:** Java 17, Spring Boot 4.0.6, MyBatis 4.0.1, Vue 3 (CDN), vanilla CSS

---

### File Structure

| File | Status | Responsibility |
|---|---|---|
| `dto/EmployeeSearchCriteria.java` | Modify | Replace `department`/`position` with `departmentIds`/`positionNames` lists |
| `mapper/DepartmentPositionMapper.java` | Modify | Add `findByDepartmentIds(List<Long>)` |
| `resources/mapper/DepartmentPositionMapper.xml` | Modify | Add `<select>` with `<foreach>` for batch position query |
| `controller/DepartmentController.java` | Modify | Add `POST /api/departments/positions-by-ids` |
| `resources/mapper/EmployeeMapper.xml` | Modify | Search WHERE: `d.id IN (...)` + `dp.position_name IN (...)` |
| `static/tlias/js/app.js` | Modify | Multi-select search data + methods |
| `static/tlias/tlias.html` | Modify | Search modal: dropdown list + tag display |
| `static/tlias/css/style.css` | Modify | `.search-dropdown`, `.tag-group`, `.tag-item` styles |

---

### Task 1: Backend — SearchCriteria + Mapper + Controller

**Files:**
- Modify: `dto/EmployeeSearchCriteria.java`
- Modify: `mapper/DepartmentPositionMapper.java`
- Modify: `resources/mapper/DepartmentPositionMapper.xml`
- Modify: `controller/DepartmentController.java`

- [ ] **Step 1: Update EmployeeSearchCriteria**

Replace `String department` and `String position` with list fields:

```java
package com.example.day04.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeSearchCriteria {
    private String name;
    private String gender;
    private List<Long> departmentIds;
    private List<String> positionNames;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private LocalDate minEntryDate;
    private LocalDate maxEntryDate;
    private int page = 1;
    private int size = 10;
}
```

- [ ] **Step 2: Add batch position query to DepartmentPositionMapper**

```java
List<DepartmentPosition> findByDepartmentIds(@Param("departmentIds") List<Long> departmentIds);
```

- [ ] **Step 3: Add batch position XML**

```xml
<select id="findByDepartmentIds" resultType="DepartmentPosition">
    SELECT DISTINCT dp.*
    FROM department_positions dp
    WHERE dp.department_id IN
    <foreach collection="departmentIds" item="id" open="(" separator="," close=")">
        #{id}
    </foreach>
</select>
```

- [ ] **Step 4: Add batch endpoint to DepartmentController**

```java
@PostMapping("/positions-by-ids")
public List<DepartmentPosition> findPositionsByIds(@RequestBody List<Long> departmentIds) {
    return departmentPositionMapper.findByDepartmentIds(departmentIds);
}
```

Add import: `import org.springframework.web.bind.annotation.PostMapping;` (if not already present via wildcard).

- [ ] **Step 5: Compile to verify**

```powershell
./mvnw.cmd compile -q
```
Expected: BUILD SUCCESS (no output)

- [ ] **Step 6: Commit**

```bash
git add src/main/java/com/example/day04/dto/EmployeeSearchCriteria.java \
        src/main/java/com/example/day04/mapper/DepartmentPositionMapper.java \
        src/main/resources/mapper/DepartmentPositionMapper.xml \
        src/main/java/com/example/day04/controller/DepartmentController.java
git commit -m "feat: search criteria supports multi-select departmentIds/positionNames"
```

---

### Task 2: Backend — EmployeeMapper XML search WHERE

**Files:**
- Modify: `resources/mapper/EmployeeMapper.xml`

- [ ] **Step 1: Replace department/position WHERE clauses**

Replace the existing:

```xml
<if test="criteria.department != null and criteria.department != ''">
    AND d.name LIKE CONCAT('%', #{criteria.department}, '%')
</if>
<if test="criteria.position != null and criteria.position != ''">
    AND dp.position_name LIKE CONCAT('%', #{criteria.position}, '%')
</if>
```

With:

```xml
<if test="criteria.departmentIds != null and criteria.departmentIds.size() > 0">
    AND d.id IN
    <foreach collection="criteria.departmentIds" item="id" open="(" separator="," close=")">
        #{id}
    </foreach>
</if>
<if test="criteria.positionNames != null and criteria.positionNames.size() > 0">
    AND dp.position_name IN
    <foreach collection="criteria.positionNames" item="pn" open="(" separator="," close=")">
        #{pn}
    </foreach>
</if>
```

Apply this change in BOTH `<sql id="searchWhere">` sections (`searchPage` and `count` use the same `<include>` — only one place to edit).

- [ ] **Step 2: Compile to verify**

```powershell
./mvnw.cmd compile -q
```
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add src/main/resources/mapper/EmployeeMapper.xml
git commit -m "feat: search WHERE uses IN (...)-based filtering for dept/position"
```

---

### Task 3: Frontend — CSS styles for dropdown and tags

**Files:**
- Modify: `static/tlias/css/style.css`

- [ ] **Step 1: Add dropdown and tag styles before `@keyframes fadeInRow`**

```css
.search-dropdown {
    position: relative;
}
.search-dropdown-trigger {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 38px;
    padding: 0 12px;
    border: 1px solid #ccc;
    border-radius: 6px;
    background: #fff;
    font-size: .9rem;
    color: #333;
    cursor: pointer;
    user-select: none;
}
.search-dropdown-trigger::after {
    content: '\25BC';
    font-size: .7rem;
    color: #999;
    margin-left: 8px;
}
.search-dropdown-trigger.open::after {
    content: '\25B2';
}
.search-dropdown-menu {
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    z-index: 10;
    background: #fff;
    border: 1px solid #ccc;
    border-radius: 6px;
    margin-top: 2px;
    max-height: 200px;
    overflow-y: auto;
    box-shadow: 0 4px 12px rgba(0,0,0,.1);
}
.search-dropdown-menu .dropdown-item {
    padding: 10px 12px;
    font-size: .9rem;
    cursor: pointer;
    transition: background .15s;
}
.search-dropdown-menu .dropdown-item:hover {
    background: #e8f0fe;
}
.tag-group {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
    margin-top: 8px;
    min-height: 28px;
}
.tag-item {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    padding: 4px 10px;
    background: #f0f2f5;
    border: 1px solid #d0d0d0;
    border-radius: 4px;
    font-size: .82rem;
    color: #555;
}
.tag-item .tag-remove {
    cursor: pointer;
    font-size: .9rem;
    line-height: 1;
    color: #999;
    margin-left: 2px;
}
.tag-item .tag-remove:hover {
    color: #e74c3c;
}
```

- [ ] **Step 2: Verify style file is valid**

No test needed — just confirm the CSS is syntactically valid (matching braces).

- [ ] **Step 3: Commit**

```bash
git add src/main/resources/static/tlias/css/style.css
git commit -m "style: add search dropdown and tag UI styles"
```

---

### Task 4: Frontend — app.js search data + methods

**Files:**
- Modify: `static/tlias/js/app.js`

- [ ] **Step 1: Add search-related data fields**

After `searchMaxEntryDate: '',` add:

```javascript
searchDepartmentIds: [],
searchPositionNames: [],
searchDeptDropdownOpen: false,
searchPosDropdownOpen: false,
searchAvailablePositions: [],
```

- [ ] **Step 2: Replace `fetchEmployees()` department/position params**

In `fetchEmployees()`, replace:

```javascript
if (this.searchDepartment) params.append('department', this.searchDepartment);
if (this.searchPosition) params.append('position', this.searchPosition);
```

With:

```javascript
this.searchDepartmentIds.forEach(id => params.append('departmentIds', id));
this.searchPositionNames.forEach(pn => params.append('positionNames', pn));
```

- [ ] **Step 3: Replace `clear()` method**

Replace the existing `clear()` with:

```javascript
clear() {
    this.searchName = '';
    this.searchGender = '';
    this.searchDepartmentIds = [];
    this.searchPositionNames = [];
    this.searchAvailablePositions = [];
    this.searchMinSalary = '';
    this.searchMaxSalary = '';
    this.searchMinEntryDate = '';
    this.searchMaxEntryDate = '';
},
```

- [ ] **Step 4: Add department dropdown methods after `closeSearchModal()`**

```javascript
toggleDeptDropdown() {
    this.searchDeptDropdownOpen = !this.searchDeptDropdownOpen;
    this.searchPosDropdownOpen = false;
},
selectSearchDept(id) {
    if (!this.searchDepartmentIds.includes(id)) {
        this.searchDepartmentIds.push(id);
        this.fetchSearchPositions();
    }
    this.searchDeptDropdownOpen = false;
},
removeSearchDept(id) {
    this.searchDepartmentIds = this.searchDepartmentIds.filter(did => did !== id);
    this.searchPositionNames = [];
    this.fetchSearchPositions();
},
```

- [ ] **Step 5: Add position dropdown methods**

```javascript
togglePosDropdown() {
    if (this.searchAvailablePositions.length > 0) {
        this.searchPosDropdownOpen = !this.searchPosDropdownOpen;
    }
},
selectSearchPosition(name) {
    if (!this.searchPositionNames.includes(name)) {
        this.searchPositionNames.push(name);
    }
    this.searchPosDropdownOpen = false;
},
removeSearchPosition(name) {
    this.searchPositionNames = this.searchPositionNames.filter(pn => pn !== name);
},
fetchSearchPositions() {
    if (this.searchDepartmentIds.length === 0) {
        this.searchAvailablePositions = [];
        return;
    }
    fetch('/api/departments/positions-by-ids', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(this.searchDepartmentIds)
    })
        .then(res => res.json())
        .then(data => {
            this.searchAvailablePositions = data;
            const validNames = new Set(data.map(p => p.positionName));
            this.searchPositionNames = this.searchPositionNames.filter(n => validNames.has(n));
        });
},
```

- [ ] **Step 6: Compile check (frontend only, no build)**

No compilation needed — this is pure JavaScript/Vue.

- [ ] **Step 7: Commit**

```bash
git add src/main/resources/static/tlias/js/app.js
git commit -m "feat: multi-select search UI data and methods"
```

---

### Task 5: Frontend — tlias.html search modal UI

**Files:**
- Modify: `static/tlias/tlias.html`

- [ ] **Step 1: Replace department/position search fields in the search modal**

Find the search modal section (around lines 81-86), replace:

```html
                <input type="text" placeholder="部门" v-model="searchDepartment">
                <input type="text" placeholder="职位" v-model="searchPosition">
```

With:

```html
                <div class="search-dropdown">
                    <div class="search-dropdown-trigger" :class="{ open: searchDeptDropdownOpen }"
                         @click.stop="toggleDeptDropdown">
                        {{ searchDepartmentIds.length > 0 ? '已选 ' + searchDepartmentIds.length + ' 个部门' : '选择部门' }}
                    </div>
                    <div class="search-dropdown-menu" v-if="searchDeptDropdownOpen"
                         @click.stop>
                        <div class="dropdown-item" v-for="d in departments" :key="d.id"
                             @click="selectSearchDept(d.id)">
                            {{ d.name }}
                        </div>
                    </div>
                    <div class="tag-group" v-if="searchDepartmentIds.length > 0">
                        <span class="tag-item" v-for="d in departments.filter(dep => searchDepartmentIds.includes(dep.id))" :key="d.id">
                            {{ d.name }}<span class="tag-remove" @click="removeSearchDept(d.id)">&times;</span>
                        </span>
                    </div>
                </div>
                <div class="search-dropdown" v-if="searchDepartmentIds.length > 0">
                    <div class="search-dropdown-trigger" :class="{ open: searchPosDropdownOpen }"
                         @click.stop="togglePosDropdown">
                        {{ searchPositionNames.length > 0 ? '已选 ' + searchPositionNames.length + ' 个职位' : '选择职位' }}
                    </div>
                    <div class="search-dropdown-menu" v-if="searchPosDropdownOpen"
                         @click.stop>
                        <div class="dropdown-item" v-for="p in searchAvailablePositions" :key="p.id"
                             @click="selectSearchPosition(p.positionName)">
                            {{ p.positionName }}
                        </div>
                    </div>
                    <div class="tag-group" v-if="searchPositionNames.length > 0">
                        <span class="tag-item" v-for="pn in searchPositionNames" :key="pn">
                            {{ pn }}<span class="tag-remove" @click.stop="removeSearchPosition(pn)">&times;</span>
                        </span>
                    </div>
                </div>
```

- [ ] **Step 2: Run existing tests to verify backend still works**

```powershell
./mvnw.cmd test -q
```
Expected: BUILD SUCCESS, 13 tests pass

- [ ] **Step 3: Commit**

```bash
git add src/main/resources/static/tlias/tlias.html
git commit -m "feat: search modal multi-select dropdown + tag UI"
```

---

### Task 6: Final verification

- [ ] **Step 1: Run full build**

```powershell
./mvnw.cmd clean test -q
```
Expected: BUILD SUCCESS, 13 tests pass

- [ ] **Step 2: Push branch**

```bash
git push origin feature/search-multiselect-dept-position
```

---

### Self-Review

| Spec Requirement | Task Coverage |
|---|---|
| Department multi-select dropdown | Task 5 — `.search-dropdown` + `selectSearchDept` |
| Selected dept shows as gray tag with × | Task 5 — `tag-group` + `removeSearchDept` |
| Position dropdown conditional on dept selection | Task 5 — `v-if="searchDepartmentIds.length > 0"` |
| Position options from selected depts merged | Task 4 — `fetchSearchPositions()` + Task 1 `findByDepartmentIds` |
| Position same tag behavior | Task 5 — `selectSearchPosition` + `removeSearchPosition` |
| Backend `IN (...)` query | Task 2 — `<foreach>` in EmployeeMapper.xml |
| SearchCriteria list fields | Task 1 — `List<Long> departmentIds`, `List<String> positionNames` |

No placeholders, no TODOs. All types consistent across tasks.
