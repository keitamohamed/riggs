package com.keita.riggs.controller;

import com.keita.riggs.model.Room;
import com.keita.riggs.model.User;
import com.keita.riggs.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/riggs/user")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(
            value = {"/add"},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addUser (
            @Valid
            @RequestBody
            User user,
            BindingResult result) {
        return service.save(user, result);
    }

    @PutMapping(
            path = {"/update/{id}"},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateUser(
            @Valid
            @RequestBody
            User user,
            BindingResult bindingResult
    )
    {
        return service.updateUser(user, bindingResult);

    }

    @GetMapping(path = {"/list"})
    public List<User> userList(HttpServletResponse response) {
        return service.userList(response);
    }

    @GetMapping(value = {"/find-by-id/{id}"})
    public Optional<User> findUserByID(@PathVariable Long id, HttpServletResponse response) {
        return service.findUserByID(id, response);
    }

    @DeleteMapping(
            path = {"/delete/{id}"}
    )
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return service.deleteUser(id);
    }

}
