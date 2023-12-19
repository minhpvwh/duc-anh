package com.example.simplecrudapp.exceptions;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final int statusCode;

    protected DomainException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}

