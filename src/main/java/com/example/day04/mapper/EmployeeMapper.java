package com.example.day04.mapper;

import com.example.day04.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmployeeMapper {
    List<Employee> searchPage(@Param("name") String name,
                              @Param("gender") String gender,
                              @Param("department") String department,
                              @Param("position") String position,
                              @Param("offset") int offset,
                              @Param("size") int size);

    long count(@Param("name") String name,
               @Param("gender") String gender,
               @Param("department") String department,
               @Param("position") String position);

    boolean delete(@Param("id") long id);

    void insert(Employee employee);
}
