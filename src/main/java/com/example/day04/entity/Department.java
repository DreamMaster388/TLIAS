package com.example.day04.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Department {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
