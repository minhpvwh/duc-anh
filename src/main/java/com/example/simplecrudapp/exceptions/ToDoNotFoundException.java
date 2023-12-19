package com.example.simplecrudapp.exceptions;

import org.springframework.http.HttpStatus;

public class ToDoNotFoundException extends DomainException {

    public ToDoNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}
