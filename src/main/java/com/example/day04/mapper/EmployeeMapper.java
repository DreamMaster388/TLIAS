package com.example.day04.mapper;

import com.example.day04.dto.EmployeeSearchCriteria;
import com.example.day04.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    List<Employee> searchPage(@Param("criteria") EmployeeSearchCriteria criteria,
                              @Param("offset") int offset,
                              @Param("size") int size);

    long count(@Param("criteria") EmployeeSearchCriteria criteria);

    void delete(@Param("id") long id);

    void insert(Employee employee);

    void edit(@Param("id") long id,
                 @Param("employee") Employee employee);
}
