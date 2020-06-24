package com.example.restapi.exception;

public class ConflictException extends RuntimeException {

    ConflictException(Class clazz, Long id) {
        super("Conflict Exception in class " + clazz.getName() + " and Id " + id);
    }
}