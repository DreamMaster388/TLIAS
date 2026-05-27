package com.example.day04.controller;

import com.example.day04.dto.EmployeeSearchCriteria;
import com.example.day04.dto.PageResult;
import com.example.day04.dto.Result;
import com.example.day04.entity.Employee;
import com.example.day04.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    private EmployeeController controller;

    private Employee createEmployee(Long id, String name) {
        Employee e = new Employee();
        e.setId(id);
        e.setName(name);
        e.setGender("男");
        e.setDepartment("教学部");
        e.setPosition("讲师");
        e.setEntryDate(LocalDate.of(2023, 9, 1));
        return e;
    }

    @BeforeEach
    void setUp() {
        controller = new EmployeeController(employeeService);
    }

    @Test
    void search_defaultPagination_shouldReturnPageResult() {
        List<Employee> employees = List.of(
                createEmployee(1L, "张三"),
                createEmployee(2L, "李四")
        );
        when(employeeService.searchPage(any(EmployeeSearchCriteria.class), anyInt(), anyInt()))
                .thenReturn(employees);
        when(employeeService.count(any(EmployeeSearchCriteria.class)))
                .thenReturn(100L);

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        Result<PageResult<Employee>> result = controller.search(criteria);

        assertEquals(1, result.getCode());
        PageResult<Employee> pr = result.getData();
        assertEquals(100, pr.getTotal());
        assertEquals(2, pr.getData().size());
    }

    @Test
    void search_withAllParams_shouldPassToService() {
        when(employeeService.searchPage(any(EmployeeSearchCriteria.class), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(employeeService.count(any(EmployeeSearchCriteria.class)))
                .thenReturn(0L);

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setName("张");
        criteria.setGender("男");
        criteria.setDepartmentIds(List.of(1L, 2L));
        criteria.setPositionNames(List.of("讲师", "班主任"));
        criteria.setPage(2);
        criteria.setSize(5);
        controller.search(criteria);

        verify(employeeService).searchPage(criteria, 5, 5);
        verify(employeeService).count(criteria);
    }

    @Test
    void search_withSalaryRange_shouldPassToService() {
        when(employeeService.searchPage(any(EmployeeSearchCriteria.class), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(employeeService.count(any(EmployeeSearchCriteria.class)))
                .thenReturn(0L);

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setMinSalary(new BigDecimal("5000"));
        criteria.setMaxSalary(new BigDecimal("15000"));
        controller.search(criteria);

        verify(employeeService).searchPage(criteria, 0, 10);
        verify(employeeService).count(criteria);
    }

    @Test
    void search_withEntryDateRange_shouldPassToService() {
        when(employeeService.searchPage(any(EmployeeSearchCriteria.class), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(employeeService.count(any(EmployeeSearchCriteria.class)))
                .thenReturn(0L);

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setMinEntryDate(LocalDate.of(2024, 1, 1));
        criteria.setMaxEntryDate(LocalDate.of(2024, 12, 31));
        controller.search(criteria);

        verify(employeeService).searchPage(criteria, 0, 10);
        verify(employeeService).count(criteria);
    }

    @Test
    void search_emptyResult_shouldReturnZeroTotal() {
        when(employeeService.searchPage(any(EmployeeSearchCriteria.class), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(employeeService.count(any(EmployeeSearchCriteria.class)))
                .thenReturn(0L);

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        Result<PageResult<Employee>> result = controller.search(criteria);

        assertEquals(1, result.getCode());
        assertEquals(0, result.getData().getTotal());
        assertTrue(result.getData().getData().isEmpty());
    }

    @Test
    void insert_success_shouldReturnOk() {
        Employee e = createEmployee(null, "新员工");
        doNothing().when(employeeService).insert(any());

        Result<Void> result = controller.insert(e);

        assertEquals(1, result.getCode());
        verify(employeeService).insert(any());
    }

    @Test
    void update_success_shouldReturnOk() {
        Employee e = createEmployee(null, "更新名");
        doNothing().when(employeeService).update(anyLong(), any());

        Result<Void> result = controller.update(1L, e);

        assertEquals(1, result.getCode());
        verify(employeeService).update(eq(1L), any());
    }

    @Test
    void delete_success_shouldReturnOk() {
        doNothing().when(employeeService).delete(anyLong());

        Result<Void> result = controller.delete(1L);

        assertEquals(1, result.getCode());
        verify(employeeService).delete(1L);
    }
}
