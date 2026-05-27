package com.example.day04.mapper;

import com.example.day04.entity.DepartmentPosition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentPositionMapper {
    List<DepartmentPosition> findByDepartmentId(@Param("departmentId") Long departmentId);
}
