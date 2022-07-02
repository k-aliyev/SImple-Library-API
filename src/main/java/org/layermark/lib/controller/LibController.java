package org.layermark.lib.controller;

import lombok.Getter;
import org.layermark.lib.service.LibService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LibController {

    LibService libService;

    public LibController(LibService libService) {
        this.libService = libService;
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

}
