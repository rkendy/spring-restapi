package com.example.restapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.restapi.model.Book;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Optional<Book> book = books.stream().filter(e -> e.getId() == id).findFirst();
        return ResponseEntity.of(book);
        // if (book == null) {
        // return ResponseEntity.notFound().build();
        // }
        // return ResponseEntity.ok(book);
    }

    @PostMapping("/book")
    public ResponseEntity<Book> newBook(@RequestBody Book book) {
        Book newBook = new Book((long) books.size() + 1, book.getTitle(), book.getAuthor());
        books.add(newBook);
        return ResponseEntity.ok(newBook);
    }

    @DeleteMapping("/book/{id}")
    ResponseEntity<?> deleteBook(@PathVariable Long id) {
        Optional<Book> book = books.stream().filter(e -> e.getId() == id).findFirst();
        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            books.remove(book.get());
            return ResponseEntity.noContent().build();
        }
    }

}