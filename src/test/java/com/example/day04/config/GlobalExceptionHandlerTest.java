package com.example.day04.config;

import com.example.day04.dto.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleException_shouldReturnErrorResult() {
        Result<Void> r = handler.handleException(new RuntimeException("test error"));

        assertEquals(0, r.getCode());
        assertEquals("test error", r.getMessage());
        assertNull(r.getData());
    }
}
