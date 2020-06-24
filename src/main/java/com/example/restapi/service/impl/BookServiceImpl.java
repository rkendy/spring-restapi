package com.example.restapi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.restapi.exception.BookException;
import com.example.restapi.model.Book;
import com.example.restapi.service.BookService;

import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final List<Book> books = new ArrayList<>();

    @Override
    public List<Book> getAll() {
        return books;
    }

    @Override
    public Book get(Long id) {
        return books.stream() //
                .filter(e -> e.getId() == id) //
                .findFirst() //
                .orElseThrow(() -> new BookException(id));
    }

    @Override
    public Book getLastCreated() {
        return books.size() > 0 ? books.get(books.size() - 1) : null;
    }

    @Override
    public Book create(Book book) {
        Book newBook = new Book((long) books.size() + 1, book.getTitle(), book.getAuthor());
        books.add(newBook);
        return newBook;
    }

    @Override
    public Book patch(Long id, Map<String, Object> bookMap) {
        Book bookToUpdate = books.stream() //
                .filter(e -> e.getId() == id) //
                .findFirst() //
                .orElseThrow(() -> new BookException(id));
        return updateValues(bookToUpdate, bookMap);
    }

    @Override
    public void update(Long id, Book book) {
        Book bookToUpdate = books.stream() //
                .filter(e -> e.getId() == id) //
                .findFirst() //
                .orElseThrow(() -> new BookException(id));
        updateValues(bookToUpdate, book);
    }

    @Override
    public void delete(Long id) {
        Optional<Book> book = books.stream().filter(e -> e.getId() == id).findFirst();
        if (book.isEmpty()) {
            throw new BookException(id);
        } else {
            books.remove(book.get());
        }
    }

    private Book updateValues(Book bookToUpdate, Map<String, Object> bookMap) {
        String title = (String) bookMap.get("title");
        String author = (String) bookMap.get("author");
        if (title != null)
            bookToUpdate.setTitle(title);
        if (author != null)
            bookToUpdate.setAuthor(author);
        return bookToUpdate;
    }

    private Book updateValues(Book bookToUpdate, Book book) {
        if (book.getTitle() != null)
            bookToUpdate.setTitle(book.getTitle());
        if (book.getAuthor() != null)
            bookToUpdate.setAuthor(book.getAuthor());

        return bookToUpdate;
    }
}