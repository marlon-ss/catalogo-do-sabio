package br.com.santander.catalogo_do_sabio.infrastructure.repository;

import br.com.santander.catalogo_do_sabio.domain.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    Page<Book> findAll(Pageable pageable);
    Book findByIsbn(String isbn);
    Page<Book> findByGenresContainingIgnoreCase(String genre, Pageable pageable);
    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
}
