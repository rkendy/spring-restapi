package com.example.restapi.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SimpleException extends RuntimeException {

    public SimpleException(String msg) {
        super(msg + " timestap: " + LocalDateTime.now());
    }

}