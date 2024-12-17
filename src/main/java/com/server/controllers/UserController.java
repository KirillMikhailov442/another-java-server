package com.server.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.dto.UserDTO;
import com.server.entities.User;
import com.server.services.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController<Pageable> {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) throws Exception {
        return new ResponseEntity<>(userService.getAll(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getOne(
            @Valid @PathVariable @Positive(message = "Incorrect id") @NotNull(message = "Id must not be null") long id)
            throws Exception {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> registration(@Valid @RequestBody UserDTO userDTO) throws Exception {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @Valid @PathVariable @Positive(message = "Incorrect id") @NotNull(message = "Id must not be null") long id,
            @Valid @RequestBody UserDTO userDTO) throws Exception {
        return new ResponseEntity<>(userService.update(id, userDTO), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOne(
            @PathVariable @Positive(message = "Incorrect id") @NotNull(message = "Id must not be null") long id)
            throws Exception {
        userService.deleteById(id);
        return new ResponseEntity<>("User Delete", HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> getMethodName(@RequestParam long id) throws Exception {
        userService.confirm(id);
        return new ResponseEntity<>("User confirm", HttpStatus.OK);
    }

}
