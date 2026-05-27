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
