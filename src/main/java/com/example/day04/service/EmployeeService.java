package com.example.day04.service;

import com.example.day04.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> searchPage(String name, String gender, String department, String position, int offset, int size);

    long count(String name, String gender, String department, String position);

    void insert(Employee employee);

    void update(Long id, Employee employee);

    void delete(Long id);
}
