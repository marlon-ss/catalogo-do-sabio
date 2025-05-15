package br.com.santander.catalogo_do_sabio.infrastructure.config;

import br.com.santander.catalogo_do_sabio.domain.model.Book;
import br.com.santander.catalogo_do_sabio.domain.model.UserApi;
import br.com.santander.catalogo_do_sabio.infrastructure.repository.BookRepository;
import br.com.santander.catalogo_do_sabio.infrastructure.repository.UserApiRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@Slf4j
public class DBInitializer implements CommandLineRunner{
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserApiRepository userRepository;
    private final Faker faker = new Faker();
    
    @Override
    @CacheEvict(value = { "books", "bookById", "booksByAuthor", "booksByGenre"}, allEntries = true)
    public void run(String... args) {
        bookRepository.deleteAll();
        userRepository.deleteAll();
        createUsers();
        createBooks();
    }

    private void createUsers() {
        List<UserApi> users = new ArrayList<>();

        UserApi admin = new UserApi("admin", new BCryptPasswordEncoder().encode("admin123"), List.of("ADMIN"));
        users.add(admin);

        UserApi user = new UserApi("user123", new BCryptPasswordEncoder().encode("pass123"), List.of("USER"));
        users.add(user);
        
        userRepository.insert(users);
        log.info("Dados de usuarios gerados com sucesso!");
    }

    private void createBooks() {
        for (int i = 0; i < 100; i++) {
            ArrayList<String> genreList = new ArrayList<>();

            Random random = new Random();

            for (int j = 0; j < random.nextInt(3) + 1; j++) {
                genreList.add(faker.book().genre());
            }

            String year = faker.number().numberBetween(1994, 2025) + "";

            Book book = new Book(
                    faker.code().isbn13(),
                    faker.book().title(),
                    faker.book().author(),
                    faker.book().publisher(),
                    year,
                    genreList,
                    faker.book().publisher()
            );
            bookRepository.save(book);
        }
        log.info("Dados de livros gerados com sucesso!");
    }
}