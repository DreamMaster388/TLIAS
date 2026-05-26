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
