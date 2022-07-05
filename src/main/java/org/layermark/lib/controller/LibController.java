package org.layermark.lib.controller;

import org.layermark.lib.dto.*;
import org.layermark.lib.model.User;
import org.layermark.lib.repository.UserRepository;
import org.layermark.lib.security.JwtTokenUtil;
import org.layermark.lib.service.LibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class LibController {
    private final AuthenticationManager authenticationManager;
    private final LibService libService;
    private final UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    public LibController(AuthenticationManager authenticationManager, LibService libService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.libService = libService;
        this.userRepository = userRepository;
    }

    @GetMapping("/books")
    public ResponseEntity getAllBooks(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(libService.getAllBooks());
    }

    @GetMapping("/users")
    public ResponseEntity getAllUsers(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(libService.getAllUsers());
    }

    @GetMapping("/authors")
    public ResponseEntity getAllAuthors(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(libService.getAllAuthors());
    }

    @GetMapping("/reservations")
    public ResponseEntity getAllReservations(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(libService.getAllReservations());
    }

    @PostMapping("/create/book")
    public ResponseEntity createBook(@RequestBody NewBookDto newBookDto)  {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(libService.createBook(newBookDto));
    }

    @PostMapping("/create/author")
    public ResponseEntity createAuthor(@RequestBody NewAuthorDto newAuthorDto)  {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(libService.createAuthor(newAuthorDto));
    }

    @PostMapping("/reservations")
    public ResponseEntity reserveBook(@RequestParam long userId, @RequestParam long bookId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(libService.reserveBook(bookId, userId));
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity returnBook(@PathVariable long reservationId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(libService.returnBook(reservationId));
    }

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) throws AuthenticationException {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        final Optional<User> user = userRepository.findByEmail(request.getEmail());
        final String token = jwtTokenUtil.generateToken(user.get());
        return ResponseEntity.ok(new TokenResponse(user.get().getEmail(), token));
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity<Boolean> signUp(@RequestBody RegistirationRequest registirationRequest) throws Exception {
        Boolean result = libService.registerUser(registirationRequest);
        return ResponseEntity.ok(result);
    }

}
