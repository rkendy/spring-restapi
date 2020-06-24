package com.example.restapi.exception;

public class BookException extends RuntimeException {
    public BookException(Long id) {
        super("Error with Book with id " + id);
    }
}