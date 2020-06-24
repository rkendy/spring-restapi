package com.example.restapi.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.restapi.exception.BookAdvice;
import com.example.restapi.exception.BookException;
import com.example.restapi.exception.ConflictAdvice;
import com.example.restapi.model.Book;
import com.example.restapi.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class BookControlerTest {

    @Mock
    BookService bookService;

    @InjectMocks
    BookController bookController;

    MockMvc mockMvc;

    Book book;

    @BeforeEach
    void setup() {
        book = new Book(1L, "Title", "Author");
        mockMvc = MockMvcBuilders.standaloneSetup(bookController) //
                .setControllerAdvice(new BookAdvice(), //
                        new ConflictAdvice())
                .build();
    }

    @Test
    void givenNoParameter_whenFindAll_thenReturnAllBooks() throws Exception {
        List<Book> list = List.of(new Book[] { new Book(1L, "T1", "A1"), new Book(2L, "T2", "A2") });

        when(bookService.getAll()).thenReturn(list);

        mockMvc.perform(get(BookController.ENDPOINT)) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.length()", is(list.size())))
                .andExpect(jsonPath("$[0].title", is(list.get(0).getTitle())))
                .andExpect(jsonPath("$[0].author", is(list.get(0).getAuthor())))
                .andExpect(jsonPath("$[1].title", is(list.get(1).getTitle())))
                .andExpect(jsonPath("$[1].author", is(list.get(1).getAuthor())));
    }

    @Test
    void givenBookId_whenFindById_thenReturnBook() throws Exception {
        when(bookService.get(anyLong())).thenReturn(book);

        mockMvc.perform(get(BookController.ENDPOINT + "/" + book.getId())) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.id").value((book.getId())));
    }

    @Test
    void givenInvalidBookId_whenFindById_thenReturnNotFound() throws Exception {
        when(bookService.get(anyLong())).thenThrow(BookException.class);

        mockMvc.perform(get(BookController.ENDPOINT + "/" + book.getId())) //
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidBook_whenCreate_thenReturnSuccess() throws Exception {
        when(bookService.create(any(Book.class))).thenReturn(book);

        String expectedLocation = "http://localhost" + BookController.ENDPOINT + "/" + book.getId();

        mockMvc.perform(post(BookController.ENDPOINT) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content(toJson(book))) //
                .andExpect(status().isCreated()) //
                .andExpect(header().string("Location", expectedLocation));
    }

    @Test
    void givenValidBook_whenPatch_thenReturnSuccess() throws Exception {

        when(bookService.patch(anyLong(), anyMap())).thenReturn(book);

        mockMvc.perform(patch(BookController.ENDPOINT + "/" + book.getId()) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content("{}")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.id").value((book.getId()))) //
                .andExpect(jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    void givenInvalidBookId_whenPatch_thenReturnNotFound() throws Exception {

        when(bookService.patch(anyLong(), anyMap())).thenThrow(BookException.class);

        mockMvc.perform(patch(BookController.ENDPOINT + "/12345") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content("{}")) //
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidBook_whenUpdate_thenReturnSuccess() throws Exception {

        doNothing().when(bookService).update(anyLong(), any(Book.class));

        mockMvc.perform(put(BookController.ENDPOINT + "/" + book.getId()) //
                .contentType(MediaType.APPLICATION_JSON) //
                .content("{}")) //
                .andExpect(status().isNoContent()) //
                .andExpect(content().string(""));
    }

    @Test
    void givenInvalidBookId_whenUpdate_thenReturnNotFound() throws Exception {

        doThrow(BookException.class).when(bookService).update(anyLong(), any(Book.class));

        mockMvc.perform(put(BookController.ENDPOINT + "/12345") //
                .contentType(MediaType.APPLICATION_JSON) //
                .content("{}")) //
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidBookId_whenDelete_thenReturnSuccess() throws Exception {

        doNothing().when(bookService).delete(anyLong());

        mockMvc.perform( //
                delete(BookController.ENDPOINT + "/" + book.getId())) //
                .andExpect(status().isNoContent()) //
                .andExpect(content().string(""));

    }

    private String toJson(Object book) {
        String result = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.writeValueAsString(book);
        } catch (JsonProcessingException e) {
        }
        return result;
    }
}