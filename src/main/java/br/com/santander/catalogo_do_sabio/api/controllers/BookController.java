package br.com.santander.catalogo_do_sabio.api.controllers;

import br.com.santander.catalogo_do_sabio.api.dto.BookDTO;
import br.com.santander.catalogo_do_sabio.domain.model.Book;
import br.com.santander.catalogo_do_sabio.domain.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/books")
@Tag(name = "Livros", description = "Operações relacionadas ao gerenciamento de livros")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    @Operation(summary = "Busca todos os livros", description = "Retorna uma lista de livros paginada")
    public ResponseEntity<List<BookDTO>>getAllBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        Page<Book> books = bookService.findAll(PageRequest.of(page, size));

        List<BookDTO> booksDTOs = books.getContent()
                .stream()
                .map(BookDTO::new)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(books.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(books.getTotalPages()))
                .header("X-Current-Page", String.valueOf(books.getNumber()))
                .header("X-Page-Size", String.valueOf(books.getSize()))
                .body(booksDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um livro pelo ISBN", description = "Retorna um livro baseado no ISBN fornecido")
    public ResponseEntity<BookDTO>getBookById(@PathVariable("id") String id) {
        Book book = bookService.findByIsbn(id);
        return ResponseEntity.ok(new BookDTO(book));
    }
    @GetMapping("genre/{genre}")
    public ResponseEntity<List<BookDTO>>getBookByGenre(@PathVariable("genre") String genre, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        Page<Book> books = bookService.findByGenresContainingIgnoreCase(genre, PageRequest.of(page, size));

        List<BookDTO> booksDTOs = books.getContent()
                .stream()
                .map(BookDTO::new)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(books.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(books.getTotalPages()))
                .header("X-Current-Page", String.valueOf(books.getNumber()))
                .header("X-Page-Size", String.valueOf(books.getSize()))
                .body(booksDTOs);
    }
    @GetMapping("author/{author}")
    public ResponseEntity<List<BookDTO>>getBookByAuthor(@PathVariable("author") String author, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        Page<Book> books = bookService.findByAuthorContainingIgnoreCase(author, PageRequest.of(page, size));

        List<BookDTO> booksDTOs = books.getContent()
                .stream()
                .map(BookDTO::new)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(books.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(books.getTotalPages()))
                .header("X-Current-Page", String.valueOf(books.getNumber()))
                .header("X-Page-Size", String.valueOf(books.getSize()))
                .body(booksDTOs);
    }
}
