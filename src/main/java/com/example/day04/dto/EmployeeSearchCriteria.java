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
