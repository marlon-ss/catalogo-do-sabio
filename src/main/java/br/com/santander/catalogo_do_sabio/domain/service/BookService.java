package br.com.santander.catalogo_do_sabio.domain.service;

import br.com.santander.catalogo_do_sabio.domain.model.Book;
import br.com.santander.catalogo_do_sabio.domain.model.error.DataNotFoundException;
import br.com.santander.catalogo_do_sabio.infrastructure.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Cacheable("books")
    public Page<Book> findAll(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        log.info("Total de livros encontrados: {}", books.getTotalElements());
        log.info("Página: {}, listando: {} livros", pageable.getPageNumber(), pageable.getPageSize());
        return books;
    }

    @Cacheable("bookById")
    public Book findByIsbn(String isbn) {
        log.info("buscando livro pelo ISBN: {}", isbn);
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new DataNotFoundException("Sem usuários com este username"));
        log.info("livro encontrado: {}", book.toString());
        return book;
    }

    @Cacheable("booksByAuthor")
    public Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable) {
        log.info("buscando livros pelo autor: {}", author);
        Page<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
        log.info("Total de livros encontrados: {} do autor: {}", books.getTotalElements(), author);
        log.info("Página: {}, listando: {} livros", pageable.getPageNumber(), pageable.getPageSize());
        return books;
    }

    @Cacheable("booksByGenre")
    public Page<Book> findByGenresContainingIgnoreCase(String genre, Pageable pageable) {
        log.info("buscando livros pelo genero: {}", genre);
        Page<Book> books = bookRepository.findByGenresContainingIgnoreCase(genre, pageable);
        log.info("Total de livros encontrados: {} para o genero: {}", books.getTotalElements(), genre);
        log.info("Página: {}, listando: {} livros", pageable.getPageNumber(), pageable.getPageSize());
        return books;
    }
}
