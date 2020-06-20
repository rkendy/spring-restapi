package com.example.restapi.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ErrorMessage {

    final private LocalDateTime timestamp = LocalDateTime.now();
    final private String message;
    final private HttpStatus status;

    public ErrorMessage(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp.toString();
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

}