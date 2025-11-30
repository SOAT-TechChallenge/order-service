package com.fiap.orderService._webApi.controller.errorHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fiap.orderService.core.domain.exceptions.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class GlobalHandlerExceptionTest {

    @InjectMocks
    private GlobalHandlerException handler;

    private enum TestEnum { VALUE_ONE, VALUE_TWO }

    @Test
    void shouldHandleGenericExceptions() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");

        ResponseEntity<ErrorResponse> response = handler.handleExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Registro não encontrado: Entity not found", response.getBody().getMessage());
    }

    @Test
    void shouldHandleSQLIntegrityConstraintViolationWithRegexMatch() {
        SQLIntegrityConstraintViolationException ex = new SQLIntegrityConstraintViolationException(
            "Some SQL error: Duplicate entry 'test@email.com' for key 'users.email'"
        );

        ResponseEntity<ErrorResponse> response = handler.handleSQLIntegrityConstraintViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Duplicate entry 'test@email.com'", response.getBody().getMessage());
    }

    @Test
    void shouldHandleSQLIntegrityConstraintViolationWithoutRegexMatch() {
        SQLIntegrityConstraintViolationException ex = new SQLIntegrityConstraintViolationException("Generic SQL Error");

        ResponseEntity<ErrorResponse> response = handler.handleSQLIntegrityConstraintViolationException(ex);

        assertEquals("Generic SQL Error", response.getBody().getMessage());
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "fieldName", "must not be null");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<HashMap<String, Object>> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        HashMap<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("errors"));
        List<?> errors = (List<?>) body.get("errors");
        assertEquals(1, errors.size());
    }

    @Test
    void shouldHandleInvalidEnumValueException() {
        InvalidFormatException cause = mock(InvalidFormatException.class);
        JsonMappingException.Reference ref = mock(JsonMappingException.Reference.class);
        
        doReturn(TestEnum.class).when(cause).getTargetType();
        when(cause.getPath()).thenReturn(Collections.singletonList(ref));
        when(ref.getFieldName()).thenReturn("status");
        
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Message", cause);

        ResponseEntity<ErrorResponse> response = handler.handleInvalidEnumValueException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Valor inválido para o campo 'status'"));
        assertTrue(response.getBody().getMessage().contains("VALUE_ONE"));
    }

    @Test
    void shouldHandleHttpMessageNotReadableExceptionGeneric() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Generic error");

        ResponseEntity<ErrorResponse> response = handler.handleInvalidEnumValueException(ex);

        assertEquals("Generic error", response.getBody().getMessage());
    }

    @Test
    void shouldHandleMethodArgumentTypeMismatchExceptionForEnum() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        
        doReturn(TestEnum.class).when(ex).getRequiredType();
        when(ex.getName()).thenReturn("category");
        when(ex.getValue()).thenReturn("INVALID_VAL");

        ResponseEntity<ErrorResponse> response = handler.handleEnumPathVariableException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Valor inválido 'INVALID_VAL'"));
    }

    @Test
    void shouldHandleMethodArgumentTypeMismatchExceptionForUUID() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        
        doReturn(UUID.class).when(ex).getRequiredType();
        when(ex.getName()).thenReturn("id");
        when(ex.getValue()).thenReturn("123-invalid");

        ResponseEntity<ErrorResponse> response = handler.handleEnumPathVariableException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Deve ser um UUID válido"));
    }

    @Test
    void shouldHandleMethodArgumentTypeMismatchExceptionGeneric() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        
        doReturn(String.class).when(ex).getRequiredType();

        ResponseEntity<ErrorResponse> response = handler.handleEnumPathVariableException(ex);

        assertEquals("Parâmetro inválido.", response.getBody().getMessage());
    }
}