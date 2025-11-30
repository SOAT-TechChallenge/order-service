package com.fiap.orderService.core.domain.entities;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para a entidade Attendant.
 */
class AttendantTest {

    // CPF válido de exemplo para uso nos testes
    private static final String VALID_CPF_NUMBER = "12345678901";
    private static final UUID VALID_ID = UUID.randomUUID();

    @Test
    void testAttendantBuild_SuccessfulCreation() {
        // Arrange
        String name = "João da Silva";
        String email = "joao.silva@example.com";

        // Act
        Attendant attendant = Attendant.build(VALID_ID, name, email, VALID_CPF_NUMBER);

        // Assert
        assertNotNull(attendant);
        assertEquals(VALID_ID, attendant.getId());
        assertEquals(name, attendant.getName());
        assertEquals(email, attendant.getEmail());
        assertNotNull(attendant.getCpf());
        // A validação completa do CPF ocorre na classe CPF, mas testamos que o objeto CPF foi criado
        assertEquals(VALID_CPF_NUMBER, attendant.getCpf().getUnformattedNumber());
    }

    @Test
    void testAttendantBuild_NullName_ThrowsException() {
        // Arrange
        String name = null;
        String email = "email@test.com";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Attendant.build(VALID_ID, name, email, VALID_CPF_NUMBER);
        });

        assertEquals("Nome obrigatório", thrown.getMessage());
    }

    @Test
    void testAttendantBuild_BlankName_ThrowsException() {
        // Arrange
        String name = " "; // Espaço em branco
        String email = "email@test.com";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Attendant.build(VALID_ID, name, email, VALID_CPF_NUMBER);
        });

        assertEquals("Nome obrigatório", thrown.getMessage());
    }

    @Test
    void testAttendantBuild_NullEmail_ThrowsException() {
        // Arrange
        String name = "Test Name";
        String email = null;

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Attendant.build(VALID_ID, name, email, VALID_CPF_NUMBER);
        });

        assertEquals("Email obrigatório", thrown.getMessage());
    }

    @Test
    void testAttendantBuild_BlankEmail_ThrowsException() {
        // Arrange
        String name = "Test Name";
        String email = ""; // String vazia

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Attendant.build(VALID_ID, name, email, VALID_CPF_NUMBER);
        });

        assertEquals("Email obrigatório", thrown.getMessage());
    }

    @Test
    void testAttendantBuild_NullCpfNumber_ThrowsException() {
        // Arrange
        String name = "Test Name";
        String email = "email@test.com";
        String cpfNumber = null;

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Attendant.build(VALID_ID, name, email, cpfNumber);
        });

        assertEquals("CPF obrigatório", thrown.getMessage());
    }

    @Test
    void testAttendantBuild_BlankCpfNumber_ThrowsException() {
        // Arrange
        String name = "Test Name";
        String email = "email@test.com";
        String cpfNumber = "  ";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Attendant.build(VALID_ID, name, email, cpfNumber);
        });

        assertEquals("CPF obrigatório", thrown.getMessage());
    }

    @Test
    void testAttendantBuild_InvalidCpfNumberFormat_ThrowsExceptionFromCPFClass() {
        // Arrange
        String name = "Test Name";
        String email = "email@test.com";
        String invalidCpf = "123"; // CPF muito curto, falhará na validação do CPF

        // Act & Assert
        // A exceção virá do construtor da classe CPF, mas o método build a propagará
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Attendant.build(VALID_ID, name, email, invalidCpf);
        });

        assertEquals("CPF inválido.", thrown.getMessage());
    }

    @Test
    void testGetters() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Maria Teste";
        String email = "maria@teste.com";
        String cpfNumber = "98765432109";
        Attendant attendant = Attendant.build(id, name, email, cpfNumber);

        // Act & Assert
        assertEquals(id, attendant.getId());
        assertEquals(name, attendant.getName());
        assertEquals(email, attendant.getEmail());
        assertEquals(cpfNumber, attendant.getCpf().getUnformattedNumber());
    }
}