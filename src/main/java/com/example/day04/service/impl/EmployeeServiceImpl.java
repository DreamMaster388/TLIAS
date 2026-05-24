package com.example.day04.service.impl;

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
    public List<Employee> searchPage(String name, String gender, String department, String position, int offset, int size) {
        return employeeMapper.searchPage(name, gender, department, position, offset, size);
    }

    @Override
    public long count(String name, String gender, String department, String position) {
        return employeeMapper.count(name, gender, department, position);
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
