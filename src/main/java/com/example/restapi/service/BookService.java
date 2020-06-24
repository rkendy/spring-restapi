package com.example.restapi.service;

import java.util.List;
import java.util.Map;

import com.example.restapi.model.Book;

public interface BookService {
    public List<Book> getAll();

    public Book get(Long id);

    public Book getLastCreated();

    public Book create(Book book);

    public Book patch(Long id, Map<String, Object> bookMap);

    public void update(Long id, Book book);

    public void delete(Long id);
}