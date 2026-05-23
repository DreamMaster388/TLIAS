package com.example.day04.controller;

import com.example.day04.dto.PageResult;
import com.example.day04.entity.Employee;
import com.example.day04.mapper.EmployeeMapper;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @GetMapping
    public PageResult<Employee> search(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) String gender,
                                       @RequestParam(required = false) String department,
                                       @RequestParam(required = false) String position,
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        int offset = (page - 1) * size;
        return new PageResult<>(
            employeeMapper.searchPage(name, gender, department, position, offset, size),
            employeeMapper.count(name, gender, department, position)
        );
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable long id) {
        return employeeMapper.delete(id);
    }

    @PostMapping
    public void insert(@RequestBody Employee employee){
        employeeMapper.insert(employee);
    }

    @PutMapping("/{id}")
    public boolean edit(@PathVariable long id, @RequestBody Employee employee) {
        return employeeMapper.edit(id, employee);
    }
}
