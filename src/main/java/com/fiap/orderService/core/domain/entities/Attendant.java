package com.fiap.orderService.core.domain.entities;

import java.util.UUID;

public class Attendant {

    private final UUID id;
    private final String name;
    private final String email;
    private final CPF cpf;

    private Attendant(UUID id, String name, String email, CPF cpf) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
    }

    public static Attendant build(UUID id, String name, String email, String cpfNumber) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome obrigatório");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email obrigatório");
        }
        if (cpfNumber == null || cpfNumber.isBlank()) {
            throw new IllegalArgumentException("CPF obrigatório");
        }

        CPF cpf = new CPF(cpfNumber);
        return new Attendant(id, name, email, cpf);
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public CPF getCpf() {
        return this.cpf;
    }

}
