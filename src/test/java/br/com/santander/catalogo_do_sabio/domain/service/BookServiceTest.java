package br.com.santander.catalogo_do_sabio.domain.service;

import br.com.santander.catalogo_do_sabio.domain.model.Book;
import br.com.santander.catalogo_do_sabio.domain.model.UserApi;
import br.com.santander.catalogo_do_sabio.domain.model.error.DataNotFoundException;
import br.com.santander.catalogo_do_sabio.infrastructure.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserApiService userService;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private UserApi user;
    private Pageable pageable;
    private Page<Book> bookPage;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Teste de Livro");
        book.setAuthor("Autor Teste");

        user = new UserApi();
        user.setId("1");
        user.setUsername("Usuário Teste");

        pageable = PageRequest.of(0, 10);
        bookPage = new PageImpl<>(Arrays.asList(book));
    }

    @Test
    void testFindAll() {
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        Page<Book> result = bookService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findAll(pageable);
    }

    @Test
    void testFindByIsbnShouldReturnBookWhenValidIsbn() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(book));
        when(userService.getCurrentUser()).thenReturn(user);

        Book result = bookService.findByIsbn("1234567890");

        assertNotNull(result);
        assertEquals("1234567890", result.getIsbn());
        verify(bookRepository).findByIsbn("1234567890");
        verify(userService).updateUser(any(UserApi.class));
    }

    @Test
    void testFindByIsbnShouldThrowExceptionWhenInvalidIsbn() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () ->
                bookService.findByIsbn("invalid-isbn")
        );
        verify(bookRepository).findByIsbn("invalid-isbn");
    }

    @Test
    void testFindByAuthor() {
        when(bookRepository.findByAuthorContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(bookPage);

        Page<Book> result = bookService.findByAuthorContainingIgnoreCase("Autor", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findByAuthorContainingIgnoreCase("Autor", pageable);
    }

    @Test
    void testFindByGenre() {
        when(bookRepository.findByGenresContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(bookPage);

        Page<Book> result = bookService.findByGenresContainingIgnoreCase("Ficção", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findByGenresContainingIgnoreCase("Ficção", pageable);
    }

    @Test
    void testGetRecentBooks() {
        LinkedList<Book> recentBooks = new LinkedList<>(Arrays.asList(book));
        user.setBooksSeen(recentBooks);

        when(userService.getCurrentUser()).thenReturn(user);

        LinkedList<Book> result = bookService.getRecentBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userService).getCurrentUser();
    }
}
