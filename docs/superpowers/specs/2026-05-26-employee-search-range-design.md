# Employee Search: Salary & Entry Date Range Filtering

## Requirement

Add salary range and entry date range filtering to the existing employee search functionality.

## Approach

**Option B**: Extract `EmployeeSearchCriteria` DTO to encapsulate search parameters, replacing scattered individual method parameters.

## Design

### 1. New DTO `EmployeeSearchCriteria`

- Package: `com.example.day04.dto`
- Fields: `name`, `gender`, `department`, `position`, `minSalary`, `maxSalary`, `minEntryDate`, `maxEntryDate`, `page`, `size`
- Spring MVC automatically binds GET query params to DTO fields

### 2. Controller (`EmployeeController.search()`)

- Replace 6 individual `@RequestParam` params with `EmployeeSearchCriteria criteria`
- Service calls updated to pass `criteria` object

### 3. Service Interface + Implementation

```java
List<Employee> searchPage(EmployeeSearchCriteria criteria, int offset, int size);
long count(EmployeeSearchCriteria criteria);
```

### 4. MyBatis XML (`EmployeeMapper.xml`)

Add 4 optional `<if>` conditions to both `searchPage` and `count` queries:

- `salary >= #{criteria.minSalary}` (if not null)
- `salary <= #{criteria.maxSalary}` (if not null)
- `entry_date >= #{criteria.minEntryDate}` (if not null)
- `entry_date <= #{criteria.maxEntryDate}` (if not null)

### 5. Frontend (`tlias.html` + `app.js`)

- Search modal: add 4 inputs — min/max salary, start/end entry date
- `fetchEmployees()`: append 4 params to query string
- `clear()`: reset new fields to empty

## Out of Scope

- Add/Edit employee pages unchanged
- Delete operation unchanged
- Pagination unchanged
- Other API endpoints unchanged
