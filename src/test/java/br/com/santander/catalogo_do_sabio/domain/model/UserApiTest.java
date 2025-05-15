package br.com.santander.catalogo_do_sabio.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UserApiTest {

    private UserApi userApi;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        userApi = new UserApi("testUser", "password", Arrays.asList("ROLE_USER"));
        book1 = new Book("123", "Livro 1", "Autor 1", "Editora 1", "2024",
                Arrays.asList("Ficção"), "Descrição 1");
        book2 = new Book("456", "Livro 2", "Autor 2", "Editora 2", "2024",
                Arrays.asList("Romance"), "Descrição 2");
    }

    @Test
    void testAddBookInitializeList() {
        userApi.setBooksSeen(null);

        userApi.addBook(book1);

        assertNotNull(userApi.getBooksSeen());
        assertEquals(1, userApi.getBooksSeen().size());
        assertEquals(book1, userApi.getBooksSeen().getFirst());
    }

    @Test
    void testAddBookFirstPosition() {
        userApi.addBook(book1);
        userApi.addBook(book2);

        assertEquals(book2, userApi.getBooksSeen().getFirst());
        assertEquals(2, userApi.getBooksSeen().size());
    }

    @Test
    void testAddBookNotDuplicates() {
        userApi.addBook(book1);
        userApi.addBook(book1);

        assertEquals(1, userApi.getBooksSeen().size());
        assertEquals(book1, userApi.getBooksSeen().getFirst());
    }

    @Test
    void testAddBookMaxFiveBooks() {
        for (int i = 1; i <= 5; i++) {
            Book book = new Book(
                    String.valueOf(i),
                    "Livro " + i,
                    "Autor",
                    "Editora",
                    "2024",
                    Arrays.asList("Gênero"),
                    "Descrição"
            );
            userApi.addBook(book);
        }
        Book book6 = new Book("6", "Livro 6", "Autor", "Editora", "2024",
                Arrays.asList("Gênero"), "Descrição");

        userApi.addBook(book6);

        assertEquals(5, userApi.getBooksSeen().size());
        assertEquals(book6, userApi.getBooksSeen().getFirst());
        assertFalse(userApi.getBooksSeen().stream()
                .anyMatch(book -> book.getIsbn().equals("1")));
    }

    @Test
    void testAddBookWhenExistsChangeToFirst() {
        userApi.addBook(book1);
        userApi.addBook(book2);

        userApi.addBook(book1);

        assertEquals(2, userApi.getBooksSeen().size());
        assertEquals(book1, userApi.getBooksSeen().getFirst());
        assertEquals(book2, userApi.getBooksSeen().getLast());
    }
}