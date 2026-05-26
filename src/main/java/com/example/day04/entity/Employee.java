package com.example.day04.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Employee {
    private Long id;
    private String name;
    private String gender;
    private String department;
    private String position;
    private BigDecimal salary;
    private LocalDate entryDate;
    private LocalDateTime lastOperationTime;
}
