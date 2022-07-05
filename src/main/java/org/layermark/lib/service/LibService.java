package org.layermark.lib.service;

import org.layermark.lib.dto.*;
import org.layermark.lib.exception.BookIsNotAvailableException;
import org.layermark.lib.exception.EntityAlreadyExistsException;
import org.layermark.lib.exception.EntityNotFoundException;
import org.layermark.lib.model.*;
import org.layermark.lib.repository.AuthorRepository;
import org.layermark.lib.repository.BookRepository;
import org.layermark.lib.repository.ReservationRepository;
import org.layermark.lib.repository.UserRepository;
import org.modelmapper.ModelMapper;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class LibService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final int noOfDaysRent= 14;


//    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LibService(BookRepository bookRepository, AuthorRepository authorRepository, UserRepository userRepository, ReservationRepository reservationRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.modelMapper = modelMapper;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<BookDto> getAllBooks(){
        List<Book> books = bookRepository.findAll();
        List<BookDto> bookDtos = new ArrayList<>();

        books.forEach(book -> bookDtos.add(BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .edition(book.getEdition())
                .publisher(book.getPublisher())
                .isAvailable(book.isAvailable())
                .authorId(book.getAuthor().getId())
                .build()));

        return bookDtos;
    }

    public List<UserDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        users.forEach(user -> userDtos.add(UserDto.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build()));
        return userDtos;
    }

    public List<AuthorDto> getAllAuthors(){
        List<Author> authors = authorRepository.findAll();
        List<AuthorDto> authorDtos = new ArrayList<>();

        authors.forEach(author -> authorDtos.add(AuthorDto.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .build()));
        return authorDtos;
    }

    public List<ReservationDto> getAllReservations(){
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationDto> reservationDtos = new ArrayList<>();

        reservations.forEach(reservation -> reservationDtos.add(ReservationDto.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .bookId(reservation.getBook().getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .build()));
        return reservationDtos;
    }

    public List<BookDto> getBooksByName(String name){
        List<Book> books = bookRepository.findByName(name);
        List<BookDto> bookDtos = new ArrayList<>();

        books.forEach(book -> bookDtos.add(BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .edition(book.getEdition())
                .publisher(book.getPublisher())
                .isAvailable(book.isAvailable())
                .authorId(book.getAuthor().getId())
                .build()));

        return bookDtos;
    }

    @Transactional
    public Boolean registerUser(RegistrationRequest registrationRequest) throws RuntimeException {

        Optional<User> existingUser = userRepository.findByEmail(registrationRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new EntityAlreadyExistsException("User with this email already exists!");
        }
        User user = new User();

        user.setEmail(registrationRequest.getEmail());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setRole(Role.getRoleByName(registrationRequest.getRole()));
        user.setPassword(bCryptPasswordEncoder.encode(registrationRequest.getPassword()));
        userRepository.save(user);
        return true;

    }

    public BookDto createBook(NewBookDto newBookDto){
        List<Book> bookChecked = bookRepository.findByName(newBookDto.getName().trim());
        if (bookChecked.size() > 0) {
            throw new EntityAlreadyExistsException("This Book Already Exists!");
        }

        Optional<Author> authorOpt = authorRepository.findById(newBookDto.getAuthorId());
        if (!authorOpt.isPresent() && authorOpt.get().getId() != newBookDto.getAuthorId()) {
            throw new EntityNotFoundException("Not valid author Id!");
        }

        newBookDto.setAvailable(true);
        Book book = modelMapper.map(newBookDto, Book.class);
        book.setAuthor(authorOpt.get());
        bookRepository.save(book);

        BookDto bookDto = new BookDto(book.getId(), book.getName(), book.getEdition(), book.getPublisher(), book.isAvailable(), authorOpt.get().getId());

        return bookDto;

    }

    public AuthorDto createAuthor(NewAuthorDto newAuthorDto){

        Optional<Author> authorOpt = authorRepository.findByFirstNameAndLastNameLike(newAuthorDto.getFirstName(), newAuthorDto.getLastName());
        if (authorOpt.isPresent()) {
            throw new EntityAlreadyExistsException("This author already exists!");
        }

        Author author = modelMapper.map(newAuthorDto, Author.class);
        authorRepository.save(author);

        AuthorDto authorDto = new AuthorDto(author.getId(), author.getFirstName(), author.getLastName());

        return authorDto;

    }
    public ReservationDto reserveBook(long bookId, long userId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Book> book = bookRepository.findById(bookId);

        if(!user.isPresent()){
            throw new EntityNotFoundException("User Not found!");
        }
        if(!book.isPresent()){
            throw new EntityNotFoundException("Book not found!");
        }
        if(!book.get().isAvailable()){
            throw new BookIsNotAvailableException("This book is not available!");
        }

        book.get().setAvailable(false);

        Calendar end = Calendar.getInstance();
        end.add(Calendar.DAY_OF_YEAR, noOfDaysRent);

        ReservationDto reservationDto = new ReservationDto(0, Calendar.getInstance().getTime(), end.getTime(), user.get().getId(), book.get().getId());
        Reservation reservation = modelMapper.map(reservationDto, Reservation.class);
        reservation.setUser(user.get());
        reservation.setBook(book.get());

        reservationRepository.save(reservation);

        reservationDto.setId(reservation.getId());

        return reservationDto;
    }

    //TODO: Instead of deleting reservations create field done, later implement history
    public ReservationDto returnBook(long reservationId){
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        if(!reservation.isPresent()){
            throw new EntityNotFoundException("Reservation Not Found!");
        }
        reservation.get().getBook().setAvailable(true);

        ReservationDto reservationDto = modelMapper.map(reservation, ReservationDto.class);

        reservationRepository.deleteById(reservationId);

        return reservationDto;
    }

}
