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
