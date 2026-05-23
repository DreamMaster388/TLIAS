package com.example.day04.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Employee {
    private Long id;
    private String name;
    private String gender;
    private String avatar;
    private String department;
    private String position;
    private LocalDate entryDate;
    private LocalDateTime lastOperationTime;
}
