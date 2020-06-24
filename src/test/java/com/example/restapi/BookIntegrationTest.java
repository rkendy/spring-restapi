package com.example.restapi;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.example.restapi.controller.BookController;
import com.example.restapi.model.Book;
import com.example.restapi.service.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    BookService bookService;

    HttpHeaders headers = new HttpHeaders();
    TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeEach
    void setup() {
        bookService.create(new Book(1L, "Title", "Author"));
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    // ***********************************************************************************
    // ** GET
    // ***********************************************************************************

    @Test
    public void givenNothing_whenGetAllBooks_thenReturnSuccess() {
        List<Book> expectedList = bookService.getAll();
        ResponseEntity<String> response = makeGetRequest(null);
        List<Book> list = fromJsonListOfBooks(response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(expectedList.size(), list.size());
        assertArrayEquals(expectedList.toArray(), list.toArray());
    }

    @Test
    public void givenValidBookId_whenGetById_thenReturnBook() {
        Book book = bookService.getAll().get(0);
        ResponseEntity<String> response = makeGetRequest(Long.toString(book.getId()));

        Book returnedBook = fromJsonToBook(response.getBody());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(returnedBook, book);
    }

    @Test
    public void givenInvalidBookId_whenGetById_thenReturnNotFound() {
        ResponseEntity<String> response = makeGetRequest("123456");
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
    }

    // ***********************************************************************************
    // ** POST
    // ***********************************************************************************
    @Test
    public void givenBook_whenCreate_thenSuccess() {
        ResponseEntity<String> response = makePostRequest("{\"title\": \"Title\", \"author\":\"Author\"}");
        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

        Book last = bookService.getLastCreated();
        assertTrue(actual.contains(BookController.ENDPOINT + "/" + last.getId()));
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
    }

    // ***********************************************************************************
    // ** DELETE
    // ***********************************************************************************
    @Test
    public void givenBookId_whenDelete_thenSuccess() {
        int sizeBefore = bookService.getAll().size();
        ResponseEntity<String> response = makeDeleteRequest("1");
        int sizeAfter = bookService.getAll().size();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
        assertEquals(sizeBefore - 1, sizeAfter);
    }

    // ***********************************************************************************
    // ** PATCH
    // ***********************************************************************************
    @Test
    public void givenBook_whenPatch_thenSuccess() {
        String json = "{\"title\": \"New Title\"}";
        Book book = bookService.get(1L);
        ResponseEntity<String> response = makePatchRequest(json, Long.toString(book.getId()));
        Book patchedBook = fromJsonToBook(response.getBody());
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(book.getAuthor(), patchedBook.getAuthor());
        assertEquals(book.getId(), patchedBook.getId());
        assertEquals("New Title", patchedBook.getTitle());
    }

    // ***********************************************************************************
    // ** PUT
    // ***********************************************************************************
    @Test
    public void givenBook_whenPut_thenSuccess() {
        String json = "{\"title\": \"New Title\"}";
        Book book = bookService.get(1L);
        ResponseEntity<String> response = makePutRequest(json, Long.toString(book.getId()));
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
    }

    private ResponseEntity<String> makeGetRequest(String bookId) {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        String urlStr = BookController.ENDPOINT + (bookId == null ? "" : "/" + bookId);
        return restTemplate.exchange(createURLWithPort(urlStr), HttpMethod.GET, entity, String.class);
    }

    private ResponseEntity<String> makePostRequest(String jsonObj) {
        HttpEntity<String> entity = new HttpEntity<String>(jsonObj, headers);
        return restTemplate.postForEntity(createURLWithPort(BookController.ENDPOINT), entity, String.class);
    }

    private ResponseEntity<String> makeDeleteRequest(String bookId) {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        String urlStr = BookController.ENDPOINT + (bookId == null ? "" : "/" + bookId);
        return restTemplate.exchange(createURLWithPort(urlStr), HttpMethod.DELETE, entity, String.class);
    }

    private ResponseEntity<String> makePatchRequest(String jsonStr, String bookId) {
        String url = createURLWithPort(BookController.ENDPOINT) + "/" + bookId;
        HttpEntity<String> entity = new HttpEntity<String>(jsonStr, headers);

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);
    }

    private ResponseEntity<String> makePutRequest(String jsonStr, String bookId) {
        HttpEntity<String> entity = new HttpEntity<String>(jsonStr, headers);
        String url = createURLWithPort(BookController.ENDPOINT) + "/" + bookId;
        return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private List<Book> fromJsonListOfBooks(String json) {
        List<Book> list = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            list = mapper.readValue(json, new TypeReference<List<Book>>() {
            });
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    private Book fromJsonToBook(String json) {
        Book book = new Book();
        try {
            ObjectMapper mapper = new ObjectMapper();
            book = mapper.readValue(json, Book.class);
        } catch (Exception e) {
            System.out.println(e);
        }
        return book;
    }
}