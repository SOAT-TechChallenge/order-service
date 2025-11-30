package com.fiap.orderService._webApi.controller.errorHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseCorrectly() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Invalid input data";

        ErrorResponse response = new ErrorResponse(status, message);

        assertNotNull(response.getTimestamp());
        assertEquals(status.value(), response.getStatus());
        assertEquals(status.getReasonPhrase(), response.getError());
        assertEquals(message, response.getMessage());
    }

    @Test
    void shouldUpdateValuesUsingSetters() {
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Original message");

        response.setStatus(404);
        response.setError("Not Found");
        response.setMessage("New message");

        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getError());
        assertEquals("New message", response.getMessage());
    }
}