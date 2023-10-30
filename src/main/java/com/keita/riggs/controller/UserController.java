package com.keita.riggs.controller;

import com.keita.riggs.model.Authenticate;
import com.keita.riggs.model.User;
import com.keita.riggs.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            BindingResult bindingResult) {
        return service.updateUser(user, bindingResult);
    }

    @PutMapping(
            path = {"/update-auth/{id}"},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateUserAuth(
            @RequestBody
            Authenticate authenticate) {
        return service.updateAuth(authenticate);
    }

    @GetMapping(path = {"/list"})
    public List<User> userList() {
        System.out.println("User list...");
        return service.userList();
    }

    @GetMapping(path = {"/find-by-id/{id}"})
    public Optional<User> findUserByID(@PathVariable Long id, HttpServletResponse response) {
        return service.findUserByID(id, response);
    }
    @GetMapping(value = {"/find-by-email/{email}"})
    public Optional<User> findUserByEmail(@PathVariable String email) {
        return service.findUserByEmail(email);
    }
    @DeleteMapping(
            path = {"/delete/{id}"}
    )
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return service.deleteUser(id);
    }

    @GetMapping(
            path = {"/user-excel-file"}
    )
    public ResponseEntity<Resource> userExcelFile() throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy_MM_dd");
        String currentDateTime = dateFormatter.format(new Date());

        String fileName = currentDateTime + "_Riggs-User-Data.xlsx";

        ByteArrayInputStream excelData = service.generateUserExcelFile();
        InputStreamResource file = new InputStreamResource(excelData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/force-download"))
                .body(file);
    }
}
