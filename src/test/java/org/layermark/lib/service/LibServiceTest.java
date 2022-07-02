package org.layermark.lib.service;

import org.junit.jupiter.api.Test;
import org.layermark.lib.model.Author;
import org.layermark.lib.model.Book;
import org.layermark.lib.model.User;
import org.layermark.lib.repository.AuthorRepository;
import org.layermark.lib.repository.BookRepository;
import org.layermark.lib.repository.ReservationRepository;
import org.layermark.lib.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LibServiceTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    void saveBook() {
        Author author = new Author(101, "Jhoan","Roaling");
        Book book = new Book(101, "Java for Dummies", "1st", "Person", true, author, null);

        bookRepository.save(book);

    }

    @Test
    void getAllBooks() {
    }

    @Test
    void saveAuthor() {
        System.out.println(authorRepository.findByFirstNameAndLastNameLike("Fed", "Das"));
    }

    @Test
    void getBooksByName() {
    }

    @Test
    void registerUser() {

        User user = new User(1, "adam@gmail.com", "password", "Adam", "Aliyev", null);

        userRepository.save(user);
    }

    @Test
    void reserveBook() {
    }
}