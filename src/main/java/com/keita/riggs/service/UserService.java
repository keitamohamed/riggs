package com.keita.riggs.service;

import com.keita.riggs.exception.AlreadyExistsException;
import com.keita.riggs.exception.NotFoundException;
import com.keita.riggs.exception.UnprocessableDataException;
import com.keita.riggs.export.UserExcel;
import com.keita.riggs.mapper.ResponseMessage;
import com.keita.riggs.model.Address;
import com.keita.riggs.model.Authenticate;
import com.keita.riggs.model.User;
import com.keita.riggs.repo.AddressRepo;
import com.keita.riggs.repo.AuthenticateRepo;
import com.keita.riggs.repo.UserRepo;
import com.keita.riggs.util.Util;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final AuthenticateRepo authenticateRepo;
    private final AddressRepo addressRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, AuthenticateRepo authenticateRepo, AddressRepo addressRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.authenticateRepo = authenticateRepo;
        this.addressRepo = addressRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> save (User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnprocessableDataException("Unable to Register User", bindingResult);
        }

        authenticateRepo.findAuthenticateByEmail(user.getAuth().getEmail())
                .ifPresent(u -> {throw new AlreadyExistsException(String.format("Email already exist { %s }", user.getAuth().getEmail()));});

        long userID = Util.generateID(9999999);
        while (isUserExist(userID).isPresent()) {
            userID = Util.generateID(9999999);
        }

        user.setUserID(userID);

        Address address = user.getAddress();
        address.setId(Util.generateID(9999999));
        address.setUser(user);

        setAuthenticate(user);
        Authenticate authenticate = user.getAuth();
        authenticate.setPassword(passwordEncoder.encode(authenticate.getPassword()));

        user.setAddress(address);
        user.setAuth(authenticate);

        User saveResult = userRepo.save(user);

        String message = String.format("New account have been created %s ", saveResult.getUserID());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(User user, BindingResult bindingResult) {
        Optional<User> getUser = isUserExist(user.getUserID());
        getUser.orElseThrow(() -> new NotFoundException(String.format("No user exist with an id %s", user.getUserID())));

        if (bindingResult.hasErrors()) {
            throw new UnprocessableDataException("Unable to update information", bindingResult);
        }

        getUser.ifPresent(u -> {
            u.setFirstName(user.getFirstName());
            u.setFirstName(user.getLastName());
            u.setPhoneNum(user.getPhoneNum());
        });



        addressRepo.save(user.getAddress());

        User updated = userRepo.save(getUser.get());
        String message = String.format("Information updated for %s, id %s", (updated.getFirstName() + " " + updated.getLastName()), updated.getUserID());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public ResponseEntity<?> updateAuth(Authenticate auth) {
        Optional<Authenticate> findAuth = isUserAuthExist(auth.getAuthID());
        findAuth.orElseThrow(() -> new NotFoundException(String.format("No Authenticate account found with id %s", auth.getAuthID())));

        findAuth.ifPresent(a -> {
            a.setPassword(passwordEncoder.encode(auth.getPassword()));
        });

        Authenticate updated = authenticateRepo.save(findAuth.get());
        String message = String.format("Successfully updated login information id: %s", updated.getAuthID());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }


    public Optional<User> findUserByID (Long id, HttpServletResponse response) {
        return userRepo.findById(id)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("No user found with an id " + id));
    }

    public Optional<User> findUserByEmail(String email) {
        Authenticate auth = authenticateRepo.findByEmail(email);
        User user = auth.getUser();
        return Optional.of(user);
    }

    public List<User> userList() {
        List<User> users = userRepo.getAllUser();
        if (users.isEmpty()) {
            throw new NotFoundException("No user available in the database");
        }
        return users;
    }

    public ResponseEntity<?> deleteUser (Long id) {
        Optional<User> findUser = isUserExist(id);
        findUser.orElseThrow(() -> new NotFoundException(String.format("No user exist with an id %s", id)));

        String message = String.format("%s with an id %s have been deleted",
                (findUser.get().getFirstName() + " " + findUser.get().getLastName()), id);
        userRepo.delete(findUser.get());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public ByteArrayInputStream generateUserExcelFile() throws IOException {
        UserExcel userExcel = new UserExcel(userList());
        return userExcel.toExcel();
    }

    private Optional<User> isUserExist(Long id) {
        return userRepo.findById(id);
    }

    private Optional<Authenticate> isUserAuthExist(Long id) {
        return authenticateRepo.findById(id);
    }

    public User getUser(Long id) {
        Optional<User> user = isUserExist(id);
        return user.orElseThrow(() -> new NotFoundException(String.format("No user exist with an id %s ", id)));
    }

    private void setAuthenticate(User user) {
        Authenticate authenticate = user.getAuth();
        authenticate.setUser(user);
        authenticate.setAuthID(Util.generateID(9999999));
        authenticate.setAccountNonExpired(true);
        authenticate.setAccountNotLocked(true);
        authenticate.setCredentialsNonExpired(true);
        authenticate.setEnabled(true);
    }

}
