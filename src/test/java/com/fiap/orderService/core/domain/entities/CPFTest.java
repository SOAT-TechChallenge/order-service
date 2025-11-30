package com.fiap.orderService.core.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CPFTest {

    private static final String UNFORMATTED_VALID = "12345678900";
    private static final String FORMATTED_VALID = "123.456.789-00";
    private static final String EXPECTED_FORMATTED = "123.456.789-00";
    private static final String INVALID_CPF = "12345";
    private static final String INVALID_PATTERN = "abc.def.ghi-jk";

    @Test
    void shouldConstructSuccessfullyWithUnformattedValidCpf() {
        CPF cpf = new CPF(UNFORMATTED_VALID);
        assertEquals(UNFORMATTED_VALID, cpf.getUnformattedNumber());
    }
    
    @Test
    void shouldConstructSuccessfullyWithFormattedValidCpf() {
        CPF cpf = new CPF(FORMATTED_VALID);
        assertEquals(UNFORMATTED_VALID, cpf.getUnformattedNumber());
    }

    @ParameterizedTest
    @ValueSource(strings = {INVALID_CPF, INVALID_PATTERN, "123.456.789-000", "123456789000"})
    void shouldThrowExceptionWhenCpfIsInvalid(String invalidCpf) {
        assertThrows(IllegalArgumentException.class, () -> 
            new CPF(invalidCpf)
        );
    }
    
    @Test
    void shouldAllowDefaultConstructor() {
        CPF cpf = new CPF();
        assertThrows(NullPointerException.class, cpf::getUnformattedNumber); 
    }

    @Test
    void shouldReturnCorrectUnformattedNumber() {
        CPF cpf = new CPF(FORMATTED_VALID);
        assertEquals(UNFORMATTED_VALID, cpf.getUnformattedNumber());
    }

    @Test
    void shouldReturnCorrectFormattedNumber() {
        CPF cpf = new CPF(UNFORMATTED_VALID);
        assertEquals(EXPECTED_FORMATTED, cpf.getFormattedNumber());
    }
    
    @Test
    void setNumberShouldThrowExceptionOnInvalidInput() {
        CPF cpf = new CPF(UNFORMATTED_VALID);
        assertThrows(IllegalArgumentException.class, () -> cpf.setNumber(INVALID_CPF));
    }
    
    @Test
    void setNumberShouldUpdateValidFormattedInput() {
        CPF cpf = new CPF(UNFORMATTED_VALID);
        cpf.setNumber(FORMATTED_VALID);
        assertEquals(UNFORMATTED_VALID, cpf.getUnformattedNumber());
    }
}