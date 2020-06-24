package com.example.restapi.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.example.restapi.model.Book;
import com.example.restapi.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * ResponseEntity<T> represents the entire HTTP response. It is possible to set
 * headers and status code.
 */
@RestController
@RequestMapping(BookController.ENDPOINT)
public class BookController {

    public static final String ENDPOINT = "/api/book";
    public static final String ENDPOINT_ID = "/{id}";

    @Autowired
    private BookService bookService;

    @GetMapping()
    public ResponseEntity<List<Book>> books() {
        return ResponseEntity.ok(bookService.getAll());
    }

    @GetMapping(ENDPOINT_ID)
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.get(id));
    }

    @PostMapping()
    public ResponseEntity<Book> newBook(@RequestBody Book book) {
        return ResponseEntity.created(getUri(bookService.create(book))).build();
    }

    @DeleteMapping(ENDPOINT_ID)
    ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(ENDPOINT_ID)
    ResponseEntity<?> patchBook(@PathVariable Long id, @RequestBody Map<String, Object> bookMap) {
        Book updatedBook = bookService.patch(id, bookMap);
        return ResponseEntity.ok(updatedBook);
    }

    @PutMapping(ENDPOINT_ID)
    ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book book) {
        bookService.update(id, book);
        return ResponseEntity.noContent().build();
    }

    private URI getUri(Book book) {
        return ServletUriComponentsBuilder //
                .fromCurrentRequest() //
                .path(ENDPOINT_ID) //
                .buildAndExpand(book.getId()) //
                .toUri();
    }

}