package com.example.restapi.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.restapi.model.Book;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ResponseEntity<T> represents the entire HTTP response. It is possible to set
 * headers and status code.
 */
@RestController
@RequestMapping("/api")
public class BookController {
    private final List<Book> books = new ArrayList<>();

    @GetMapping("/book")
    public ResponseEntity<List<Book>> books() {
        return ResponseEntity.ok(books);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBook() {
        return ResponseEntity.ok(books.get(0));
    }

    @PostMapping("/book")
    public ResponseEntity<Book> newBook(@RequestBody Book book) {
        Book newBook = new Book(book.getTitle(), book.getAuthor());
        books.add(newBook);
        return ResponseEntity.ok(newBook);
    }

}