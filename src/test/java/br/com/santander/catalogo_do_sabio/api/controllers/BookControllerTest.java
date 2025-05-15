package br.com.santander.catalogo_do_sabio.api.controllers;

import br.com.santander.catalogo_do_sabio.api.dto.BookDTO;
import br.com.santander.catalogo_do_sabio.domain.model.Book;
import br.com.santander.catalogo_do_sabio.domain.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private Book testBook;
    private List<Book> bookList;
    private Page<Book> bookPage;

    @BeforeEach
    void setUp() {
        testBook = new Book(
                "123",
                "Livro Teste",
                "Autor Teste",
                "Editora Teste",
                "2024",
                Arrays.asList("Ficção"),
                "Descrição Teste"
        );

        bookList = Arrays.asList(testBook);
        bookPage = new PageImpl<>(bookList);
    }

    @Test
    void testGetAllBooks() {
        when(bookService.findAll(any(PageRequest.class))).thenReturn(bookPage);

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks(0, 50);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        assertTrue(response.getHeaders().containsKey("X-Total-Count"));
        assertTrue(response.getHeaders().containsKey("X-Total-Pages"));
        assertTrue(response.getHeaders().containsKey("X-Current-Page"));
        assertTrue(response.getHeaders().containsKey("X-Page-Size"));
    }

    @Test
    void testGetBookById() {
        when(bookService.findByIsbn("123")).thenReturn(testBook);

        ResponseEntity<BookDTO> response = bookController.getBookById("123");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("123", response.getBody().getIsbn());
    }

    @Test
    void testGetBookByGenre() {
        when(bookService.findByGenresContainingIgnoreCase(anyString(), any(PageRequest.class)))
                .thenReturn(bookPage);

        ResponseEntity<List<BookDTO>> response = bookController.getBookByGenre("Ficção", 0, 50);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        assertTrue(response.getHeaders().containsKey("X-Total-Count"));
        assertTrue(response.getHeaders().containsKey("X-Total-Pages"));
        assertTrue(response.getHeaders().containsKey("X-Current-Page"));
        assertTrue(response.getHeaders().containsKey("X-Page-Size"));
    }

    @Test
    void testGetBookByAuthor() {
        when(bookService.findByAuthorContainingIgnoreCase(anyString(), any(PageRequest.class)))
                .thenReturn(bookPage);

        ResponseEntity<List<BookDTO>> response = bookController.getBookByAuthor("Autor", 0, 50);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        assertTrue(response.getHeaders().containsKey("X-Total-Count"));
        assertTrue(response.getHeaders().containsKey("X-Total-Pages"));
        assertTrue(response.getHeaders().containsKey("X-Current-Page"));
        assertTrue(response.getHeaders().containsKey("X-Page-Size"));
    }

    @Test
    void testGetRecentBooks() {
        LinkedList<Book> recentBooks = new LinkedList<>(bookList);
        when(bookService.getRecentBooks()).thenReturn(recentBooks);

        ResponseEntity<List<BookDTO>> response = bookController.getRecentBooks();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAllBooksVerifyHeaders() {
        when(bookService.findAll(any(PageRequest.class))).thenReturn(bookPage);

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks(0, 50);

        HttpHeaders headers = response.getHeaders();
        assertEquals("1", headers.getFirst("X-Total-Count"));
        assertEquals("1", headers.getFirst("X-Total-Pages"));
        assertEquals("0", headers.getFirst("X-Current-Page"));
        assertEquals("1", headers.getFirst("X-Page-Size"));
    }

    @Test
    void testConvertBookToDTO() {
        when(bookService.findByIsbn("123")).thenReturn(testBook);

        ResponseEntity<BookDTO> response = bookController.getBookById("123");
        BookDTO dto = response.getBody();

        assertNotNull(dto);
        assertEquals(testBook.getIsbn(), dto.getIsbn());
        assertEquals(testBook.getTitle(), dto.getTitle());
        assertEquals(testBook.getAuthor(), dto.getAuthor());
        assertEquals(testBook.getPublisher(), dto.getPublisher());
        assertEquals(testBook.getYear(), dto.getYear());
        assertEquals(testBook.getGenres(), dto.getGenres());
        assertEquals(testBook.getDescription(), dto.getDescription());
    }
}