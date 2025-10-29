package com.fiap.orderService.core.domain.exceptions;

public class InvalidOrderStatusTransitionException extends DomainException {

    public InvalidOrderStatusTransitionException(String message) {
        super(message);
    }
}
