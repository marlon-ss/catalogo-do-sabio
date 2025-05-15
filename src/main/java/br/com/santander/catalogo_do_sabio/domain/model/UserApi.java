package br.com.santander.catalogo_do_sabio.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Document(collection = "users")
public class UserApi implements Serializable {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String password;
    private List<String> roles;
    private LinkedList<Book> booksSeen = new LinkedList<>();

    public UserApi() {}

    public UserApi(String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserApi{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }

    public void addBook(Book lastBookSeen) {
        if (booksSeen == null) {
            booksSeen = new LinkedList<>();
        }

        booksSeen.removeIf(book -> book.getIsbn().equals(lastBookSeen.getIsbn()));

        if (booksSeen.size() >= 5) {
            booksSeen.removeLast();
        }

        booksSeen.addFirst(lastBookSeen);
    }

}