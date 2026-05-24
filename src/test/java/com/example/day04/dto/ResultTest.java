package com.example.day04.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void successWithData_shouldSetCorrectFields() {
        Result<String> r = Result.success("hello");

        assertEquals(1, r.getCode());
        assertEquals("success", r.getMessage());
        assertEquals("hello", r.getData());
    }

    @Test
    void successWithoutData_shouldSetDataToNull() {
        Result<String> r = Result.success();

        assertEquals(1, r.getCode());
        assertEquals("success", r.getMessage());
        assertNull(r.getData());
    }

    @Test
    void error_shouldSetCodeAndMessage() {
        Result<String> r = Result.error(0, "something went wrong");

        assertEquals(0, r.getCode());
        assertEquals("something went wrong", r.getMessage());
        assertNull(r.getData());
    }
}
