package com.example.restapi.model;

import java.util.Objects;

public class Book {
    private Long id;
    private String title;
    private String author;

    public Book() {

    }

    public Book(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Book book = (Book) obj;
        return this.id == book.id && Objects.equals(title, book.title) && Objects.equals(author, book.author);
    }

}