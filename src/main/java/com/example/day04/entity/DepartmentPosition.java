package com.example.day04.entity;

import lombok.Data;

@Data
public class DepartmentPosition {
    private Long id;
    private Long departmentId;
    private String positionName;
}
