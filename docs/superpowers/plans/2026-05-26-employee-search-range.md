# Employee Search Range Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add salary range (`minSalary`/`maxSalary`) and entry date range (`minEntryDate`/`maxEntryDate`) filtering to the employee search.

**Architecture:** Extract `EmployeeSearchCriteria` DTO to encapsulate all search params, propagate through Controller → Service → MyBatis XML, and add corresponding UI inputs.

**Tech Stack:** Spring Boot 4.0.6, MyBatis 4.0.1, Vue 3 (CDN), Java 17

---

### Task 1: Create `EmployeeSearchCriteria` DTO

**Files:**
- Create: `src/main/java/com/example/day04/dto/EmployeeSearchCriteria.java`

- [ ] **Step 1: Create the DTO class**

```java
package com.example.day04.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeSearchCriteria {
    private String name;
    private String gender;
    private String department;
    private String position;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private LocalDate minEntryDate;
    private LocalDate maxEntryDate;
    private int page = 1;
    private int size = 10;
}
```

- [ ] **Step 2: Run compile to verify**

Run: `./mvnw.cmd compile -q`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/example/day04/dto/EmployeeSearchCriteria.java
git commit -m "feat: add EmployeeSearchCriteria DTO"
```

---

### Task 2: Update Mapper interface, Service interface and implementation

**Files:**
- Modify: `src/main/java/com/example/day04/mapper/EmployeeMapper.java`
- Modify: `src/main/java/com/example/day04/service/EmployeeService.java`
- Modify: `src/main/java/com/example/day04/service/impl/EmployeeServiceImpl.java`

- [ ] **Step 1: Update `EmployeeService` interface**

Change `searchPage` and `count` signatures to use `EmployeeSearchCriteria`:

```java
package com.example.day04.service;

import com.example.day04.dto.EmployeeSearchCriteria;
import com.example.day04.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> searchPage(EmployeeSearchCriteria criteria, int offset, int size);

    long count(EmployeeSearchCriteria criteria);

    void insert(Employee employee);

    void update(Long id, Employee employee);

    void delete(Long id);
}
```

- [ ] **Step 2: Update `EmployeeMapper` interface**

Replace the individual params with `EmployeeSearchCriteria`:

```java
package com.example.day04.mapper;

import com.example.day04.dto.EmployeeSearchCriteria;
import com.example.day04.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    List<Employee> searchPage(@Param("criteria") EmployeeSearchCriteria criteria,
                              @Param("offset") int offset,
                              @Param("size") int size);

    long count(@Param("criteria") EmployeeSearchCriteria criteria);

    void delete(@Param("id") long id);

    void insert(Employee employee);

    void edit(@Param("id") long id,
                 @Param("employee") Employee employee);
}
```

- [ ] **Step 3: Update `EmployeeServiceImpl`**

```java
package com.example.day04.service.impl;

import com.example.day04.dto.EmployeeSearchCriteria;
import com.example.day04.entity.Employee;
import com.example.day04.mapper.EmployeeMapper;
import com.example.day04.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<Employee> searchPage(EmployeeSearchCriteria criteria, int offset, int size) {
        return employeeMapper.searchPage(criteria, offset, size);
    }

    @Override
    public long count(EmployeeSearchCriteria criteria) {
        return employeeMapper.count(criteria);
    }

    @Override
    public void insert(Employee employee) {
        employeeMapper.insert(employee);
    }

    @Override
    public void update(Long id, Employee employee) {
        employeeMapper.edit(id, employee);
    }

    @Override
    public void delete(Long id) {
        employeeMapper.delete(id);
    }
}
```

- [ ] **Step 4: Run compile to verify**

Run: `./mvnw.cmd compile -q`
Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/example/day04/mapper/EmployeeMapper.java src/main/java/com/example/day04/service/EmployeeService.java src/main/java/com/example/day04/service/impl/EmployeeServiceImpl.java
git commit -m "refactor: update mapper and service to use EmployeeSearchCriteria"
```

---

### Task 3: Update Controller and tests

**Files:**
- Modify: `src/main/java/com/example/day04/controller/EmployeeController.java`
- Modify: `src/test/java/com/example/day04/controller/EmployeeControllerTest.java`

- [ ] **Step 1: Update `EmployeeControllerTest` to use `EmployeeSearchCriteria`**

Replace the entire file:

```java
package com.example.day04.controller;

import com.example.day04.dto.EmployeeSearchCriteria;
import com.example.day04.dto.PageResult;
import com.example.day04.dto.Result;
import com.example.day04.entity.Employee;
import com.example.day04.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    private EmployeeController controller;

    private Employee createEmployee(Long id, String name) {
        Employee e = new Employee();
        e.setId(id);
        e.setName(name);
        e.setGender("男");
        e.setDepartment("教学部");
        e.setPosition("讲师");
        e.setEntryDate(LocalDate.of(2023, 9, 1));
        return e;
    }

    @BeforeEach
    void setUp() {
        controller = new EmployeeController(employeeService);
    }

    @Test
    void search_defaultPagination_shouldReturnPageResult() {
        List<Employee> employees = List.of(
                createEmployee(1L, "张三"),
                createEmployee(2L, "李四")
        );
        when(employeeService.searchPage(any(EmployeeSearchCriteria.class), anyInt(), anyInt()))
                .thenReturn(employees);
        when(employeeService.count(any(EmployeeSearchCriteria.class)))
                .thenReturn(100L);

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        Result<PageResult<Employee>> result = controller.search(criteria);

        assertEquals(1, result.getCode());
        PageResult<Employee> pr = result.getData();
        assertEquals(100, pr.getTotal());
        assertEquals(2, pr.getData().size());
    }

    @Test
    void search_withAllParams_shouldPassToService() {
        when(employeeService.searchPage(any(EmployeeSearchCriteria.class), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(employeeService.count(any(EmployeeSearchCriteria.class)))
                .thenReturn(0L);

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setName("张");
        criteria.setGender("男");
        criteria.setDepartment("教学");
        criteria.setPosition("讲师");
        criteria.setPage(2);
        criteria.setSize(5);
        controller.search(criteria);

        verify(employeeService).searchPage(criteria, 5, 5);
        verify(employeeService).count(criteria);
    }

    @Test
    void search_withSalaryRange_shouldPassToService() {
        when(employeeService.searchPage(any(EmployeeSearchCriteria.class), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(employeeService.count(any(EmployeeSearchCriteria.class)))
                .thenReturn(0L);

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setMinSalary(new BigDecimal("5000"));
        criteria.setMaxSalary(new BigDecimal("15000"));
        controller.search(criteria);

        verify(employeeService).searchPage(criteria, 0, 10);
        verify(employeeService).count(criteria);
    }

    @Test
    void search_withEntryDateRange_shouldPassToService() {
        when(employeeService.searchPage(any(EmployeeSearchCriteria.class), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(employeeService.count(any(EmployeeSearchCriteria.class)))
                .thenReturn(0L);

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setMinEntryDate(LocalDate.of(2024, 1, 1));
        criteria.setMaxEntryDate(LocalDate.of(2024, 12, 31));
        controller.search(criteria);

        verify(employeeService).searchPage(criteria, 0, 10);
        verify(employeeService).count(criteria);
    }

    @Test
    void search_emptyResult_shouldReturnZeroTotal() {
        when(employeeService.searchPage(any(EmployeeSearchCriteria.class), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(employeeService.count(any(EmployeeSearchCriteria.class)))
                .thenReturn(0L);

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        Result<PageResult<Employee>> result = controller.search(criteria);

        assertEquals(1, result.getCode());
        assertEquals(0, result.getData().getTotal());
        assertTrue(result.getData().getData().isEmpty());
    }

    @Test
    void insert_success_shouldReturnOk() {
        Employee e = createEmployee(null, "新员工");
        doNothing().when(employeeService).insert(any());

        Result<Void> result = controller.insert(e);

        assertEquals(1, result.getCode());
        verify(employeeService).insert(any());
    }

    @Test
    void update_success_shouldReturnOk() {
        Employee e = createEmployee(null, "更新名");
        doNothing().when(employeeService).update(anyLong(), any());

        Result<Void> result = controller.update(1L, e);

        assertEquals(1, result.getCode());
        verify(employeeService).update(eq(1L), any());
    }

    @Test
    void delete_success_shouldReturnOk() {
        doNothing().when(employeeService).delete(anyLong());

        Result<Void> result = controller.delete(1L);

        assertEquals(1, result.getCode());
        verify(employeeService).delete(1L);
    }
}
```

- [ ] **Step 2: Run the failing test**

Run: `./mvnw.cmd test -Dtest=EmployeeControllerTest -q`
Expected: COMPILATION FAILURE (controller.search() signature changed)

- [ ] **Step 3: Update `EmployeeController`**

```java
package com.example.day04.controller;

import com.example.day04.dto.EmployeeSearchCriteria;
import com.example.day04.dto.PageResult;
import com.example.day04.dto.Result;
import com.example.day04.entity.Employee;
import com.example.day04.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public Result<PageResult<Employee>> search(EmployeeSearchCriteria criteria) {
        int offset = (criteria.getPage() - 1) * criteria.getSize();
        PageResult<Employee> pr = new PageResult<>(
                employeeService.searchPage(criteria, offset, criteria.getSize()),
                employeeService.count(criteria)
        );
        return Result.success(pr);
    }

    @PostMapping
    public Result<Void> insert(@RequestBody Employee employee) {
        employeeService.insert(employee);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Employee employee) {
        employeeService.update(id, employee);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return Result.success();
    }
}
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `./mvnw.cmd test -Dtest=EmployeeControllerTest -q`
Expected: All tests pass (green)

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/example/day04/controller/EmployeeController.java src/test/java/com/example/day04/controller/EmployeeControllerTest.java
git commit -m "refactor: update controller to use EmployeeSearchCriteria"
```

---

### Task 4: Update MyBatis XML with new conditions

**Files:**
- Modify: `src/main/resources/mapper/EmployeeMapper.xml`

- [ ] **Step 1: Update `EmployeeMapper.xml`**

Add 4 new `<if>` conditions inside `<where>` for both `searchPage` and `count` queries, and update param references to use `criteria.*`:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.day04.mapper.EmployeeMapper">
    <select id="searchPage" resultType="Employee">
        SELECT * FROM employee
        <where>
            <if test="criteria.name != null and criteria.name != ''">
                AND name LIKE CONCAT('%', #{criteria.name}, '%')
            </if>
            <if test="criteria.gender != null and criteria.gender != ''">
                AND gender = #{criteria.gender}
            </if>
            <if test="criteria.department != null and criteria.department != ''">
                AND department LIKE CONCAT('%', #{criteria.department}, '%')
            </if>
            <if test="criteria.position != null and criteria.position != ''">
                AND position LIKE CONCAT('%', #{criteria.position}, '%')
            </if>
            <if test="criteria.minSalary != null">
                AND salary &gt;= #{criteria.minSalary}
            </if>
            <if test="criteria.maxSalary != null">
                AND salary &lt;= #{criteria.maxSalary}
            </if>
            <if test="criteria.minEntryDate != null">
                AND entry_date &gt;= #{criteria.minEntryDate}
            </if>
            <if test="criteria.maxEntryDate != null">
                AND entry_date &lt;= #{criteria.maxEntryDate}
            </if>
        </where>
        LIMIT #{size} OFFSET #{offset}
    </select>

    <select id="count" resultType="long">
        SELECT COUNT(*) FROM employee
        <where>
            <if test="criteria.name != null and criteria.name != ''">
                AND name LIKE CONCAT('%', #{criteria.name}, '%')
            </if>
            <if test="criteria.gender != null and criteria.gender != ''">
                AND gender = #{criteria.gender}
            </if>
            <if test="criteria.department != null and criteria.department != ''">
                AND department LIKE CONCAT('%', #{criteria.department}, '%')
            </if>
            <if test="criteria.position != null and criteria.position != ''">
                AND position LIKE CONCAT('%', #{criteria.position}, '%')
            </if>
            <if test="criteria.minSalary != null">
                AND salary &gt;= #{criteria.minSalary}
            </if>
            <if test="criteria.maxSalary != null">
                AND salary &lt;= #{criteria.maxSalary}
            </if>
            <if test="criteria.minEntryDate != null">
                AND entry_date &gt;= #{criteria.minEntryDate}
            </if>
            <if test="criteria.maxEntryDate != null">
                AND entry_date &lt;= #{criteria.maxEntryDate}
            </if>
        </where>
    </select>

    <delete id="delete">
        DELETE FROM employee WHERE id = #{id}
    </delete>

    <insert id="insert">
        INSERT INTO employee (name, gender, department, position, salary, entry_date, last_operation_time)
        VALUES (#{name}, #{gender}, #{department}, #{position}, #{salary}, #{entryDate}, NOW())
    </insert>

    <update id="edit">
        UPDATE employee
        SET name = #{employee.name},
            gender = #{employee.gender},
            department = #{employee.department},
            position = #{employee.position},
            salary = #{employee.salary},
            entry_date = #{employee.entryDate},
            last_operation_time = NOW()
        WHERE id = #{id}
    </update>
</mapper>
```

- [ ] **Step 2: Run compile to verify**

Run: `./mvnw.cmd compile -q`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit**

```bash
git add src/main/resources/mapper/EmployeeMapper.xml
git commit -m "feat: add salary and date range conditions to mapper XML"
```

---

### Task 5: Update Frontend

**Files:**
- Modify: `src/main/resources/static/tlias/js/app.js`
- Modify: `src/main/resources/static/tlias/tlias.html`

- [ ] **Step 1: Update `app.js`**

Add 4 new data fields, update `fetchEmployees()` params, and update `clear()`:

```javascript
const PAGE_SIZE = 10;

const app = Vue.createApp({
    data() {
        return {
            employees: [],
            total: 0,
            searchName: '',
            searchGender: '',
            searchDepartment: '',
            searchPosition: '',
            searchMinSalary: '',
            searchMaxSalary: '',
            searchMinEntryDate: '',
            searchMaxEntryDate: '',
            currentPage: 1,
            jumpPage: null,
            showSearchModal: false,
            deleteTarget: null,
            showDeleteModal: false,
            showAddModal: false,
            showEditModal: false,
            editTarget: null,
            addForm: {
                name: '',
                gender: '男',
                department: '',
                position: '',
                salary: null,
                entryDate: ''
            },
            editForm: {
                name: '',
                gender: '男',
                department: '',
                position: '',
                salary: null,
                entryDate: ''
            },
        }
    },
    computed: {
        totalPages() {
            return Math.ceil(this.total / PAGE_SIZE);
        },
        pageData() {
            return this.employees;
        }
    },
    methods: {
        openSearchModal() {
            this.showSearchModal = true;
        },
        closeSearchModal() {
            this.showSearchModal = false;
        },
        fetchEmployees() {
            const params = new URLSearchParams();
            if (this.searchName) params.append('name', this.searchName);
            if (this.searchGender) params.append('gender', this.searchGender);
            if (this.searchDepartment) params.append('department', this.searchDepartment);
            if (this.searchPosition) params.append('position', this.searchPosition);
            if (this.searchMinSalary) params.append('minSalary', this.searchMinSalary);
            if (this.searchMaxSalary) params.append('maxSalary', this.searchMaxSalary);
            if (this.searchMinEntryDate) params.append('minEntryDate', this.searchMinEntryDate);
            if (this.searchMaxEntryDate) params.append('maxEntryDate', this.searchMaxEntryDate);
            params.append('page', this.currentPage);
            params.append('size', PAGE_SIZE);

            fetch(`/api/employees?${params.toString()}`)
                .then(res => res.json())
                .then(result => {
                    if (result.code === 1) {
                        this.employees = result.data.data;
                        this.total = result.data.total;
                    }
                });
        },
        clear() {
            this.searchName = '';
            this.searchGender = '';
            this.searchDepartment = '';
            this.searchPosition = '';
            this.searchMinSalary = '';
            this.searchMaxSalary = '';
            this.searchMinEntryDate = '';
            this.searchMaxEntryDate = '';
        },
        search() {
            this.currentPage = 1;
            this.showSearchModal = false;
            this.fetchEmployees();
        },
        goPage(p) {
            if (typeof p !== 'number' || p < 1 || p > this.totalPages) return;
            this.currentPage = p;
            this.fetchEmployees();
        },
        jumpToPage() {
            const p = this.jumpPage;
            if (p === null || p === '' || isNaN(p)) return;
            this.goPage(p);
            this.jumpPage = null;
        },
        deleteRow(d) {
            this.deleteTarget = d;
            this.showDeleteModal = true;
        },
        confirmDelete() {
            const id = this.deleteTarget.id;
            fetch(`/api/employees/${id}`,
            { method: 'DELETE' })
                .then(() => {
                    this.showDeleteModal = false;
                    this.deleteTarget = null;
                    this.fetchEmployees();
                });
        },
        cancelDelete() {
            this.showDeleteModal = false;
            this.deleteTarget = null;
        },
        openEditModal(d){
            this.editTarget = d;
            this.editForm = { name: d.name, gender: d.gender, department: d.department, position: d.position, salary: d.salary, entryDate: d.entryDate };
            this.showEditModal = true;
        },
        closeEditModal() {
            this.showEditModal = false;
        },
        editEmployee() {
            const id = this.editTarget.id;
            fetch(`/api/employees/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.editForm)
            }).then(() => {
                this.showEditModal = false;
                this.fetchEmployees();
            });
        },
        openAddModal() {
            this.addForm = { name: '', gender: '男', department: '', position: '', salary: null, entryDate: '' };
            this.showAddModal = true;
        },
        closeAddModal() {
            this.showAddModal = false;
        },
        saveEmployee() {
            if (!this.addForm.name) return;
            fetch('/api/employees', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.addForm)
            }).then(() => {
                this.showAddModal = false;
                this.fetchEmployees();
            });
        }
    },
    mounted() {
        this.fetchEmployees();
    }
});

app.mount('#app');
```

- [ ] **Step 2: Update `tlias.html` search modal**

Add 4 new inputs to the search modal's `.modal-form` div after the position input:

```html
<div class="modal-overlay" v-if="showSearchModal" @click.self="closeSearchModal">
    <div class="modal-box">
        <h3>搜索条件</h3>
        <div class="modal-form">
            <input type="text" placeholder="姓名" v-model="searchName">
            <select v-model="searchGender">
                <option value="">性别</option>
                <option>男</option>
                <option>女</option>
            </select>
            <input type="text" placeholder="部门" v-model="searchDepartment">
            <input type="text" placeholder="职位" v-model="searchPosition">
            <input type="number" step="0.01" placeholder="最低月薪" v-model="searchMinSalary">
            <input type="number" step="0.01" placeholder="最高月薪" v-model="searchMaxSalary">
            <input type="date" placeholder="入职起始日期" v-model="searchMinEntryDate">
            <input type="date" placeholder="入职结束日期" v-model="searchMaxEntryDate">
        </div>
        <div class="modal-actions">
            <button class="btn-search" @click="search">搜索</button>
            <button class="btn-clear" @click="clear">清空</button>
            <button class="btn-clear" @click="closeSearchModal">取消</button>
        </div>
    </div>
</div>
```

- [ ] **Step 3: Commit**

```bash
git add src/main/resources/static/tlias/js/app.js src/main/resources/static/tlias/tlias.html
git commit -m "feat: add salary and date range inputs to search modal"
```

---

### Task 6: Verify full build

- [ ] **Step 1: Run all tests**

Run: `./mvnw.cmd test`
Expected: All tests pass

- [ ] **Step 2: Final commit**

```bash
git add -A
git commit -m "chore: finalize search range feature"
```
