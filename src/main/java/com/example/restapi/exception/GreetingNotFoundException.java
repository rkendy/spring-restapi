package com.example.restapi.exception;

public class GreetingNotFoundException extends RuntimeException {
    public GreetingNotFoundException(Long id) {
        super("Error finding Greeting of id " + id);
    }
    
}