package com.example.day04.controller;

import com.example.day04.entity.Department;
import com.example.day04.entity.DepartmentPosition;
import com.example.day04.mapper.DepartmentMapper;
import com.example.day04.mapper.DepartmentPositionMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentMapper departmentMapper;
    private final DepartmentPositionMapper departmentPositionMapper;

    public DepartmentController(DepartmentMapper departmentMapper, DepartmentPositionMapper departmentPositionMapper) {
        this.departmentMapper = departmentMapper;
        this.departmentPositionMapper = departmentPositionMapper;
    }

    @GetMapping
    public List<Department> findAll() {
        return departmentMapper.findAll();
    }

    @GetMapping("/{id}/positions")
    public List<DepartmentPosition> findPositions(@PathVariable Long id) {
        return departmentPositionMapper.findByDepartmentId(id);
    }

    @PostMapping("/positions-by-ids")
    public List<DepartmentPosition> findPositionsByIds(@RequestBody List<Long> departmentIds) {
        return departmentPositionMapper.findByDepartmentIds(departmentIds);
    }
}
