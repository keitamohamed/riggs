package com.keita.riggs.service;

import com.keita.riggs.handler.ErrorMessage;
import com.keita.riggs.handler.InvalidInput;
import com.keita.riggs.handler.RoomExceptHandler;
import com.keita.riggs.mapper.ResponseMessage;
import com.keita.riggs.model.Address;
import com.keita.riggs.model.User;
import com.keita.riggs.repo.UserRepo;
import com.keita.riggs.util.Util;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public ResponseEntity<?> save (User user, BindingResult result) {
        if (result.hasErrors()) {
            return InvalidInput.userError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        long userID = Util.generateID(9999999);
        while (isUserExist(userID).isPresent()) {
            userID = Util.generateID(9999999);
        }
        user.setUserID(userID);
        Address address = user.getAddress();
        address.setId(Util.generateID(9999999));
        address.setUser(user);
        user.setAddress(address);
        User saveResult = userRepo.save(user);
        String message = String.format("New user have been added with an id %s ", saveResult.getUserID());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(Long id, User user) {
        Optional<User> getUser = isUserExist(id);
        ResponseEntity<ResponseMessage> responseMessage1 = userDoesNotExist(id);
        if (getUser.isEmpty()) {
            return responseMessage1;
        }
        getUser.ifPresent(u -> {
            u.setFirstName(user.getFirstName());
            u.setFirstName(user.getLastName());
            u.setEmail(user.getEmail());
            u.setPhoneNum(user.getPhoneNum());

        });
        User updated = userRepo.save(getUser.get());
        String message = String.format("Information updated for %s,  id %s", (updated.getFirstName() + " " + updated.getLastName()), updated.getUserID());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public Optional<User> findUserByID (Long id, HttpServletResponse response) {
        String message = "No user found with an id " + id;
        return userRepo.findById(id)
                .map(Optional::of)
                .orElseThrow(() -> new RoomExceptHandler(HttpStatus.UNPROCESSABLE_ENTITY, response, message));
    }

    public List<User> userList(HttpServletResponse response) {
        List<User> users = userRepo.getAllUser();
        if (users.isEmpty()) {
            throw new ErrorMessage(response, HttpStatus.ACCEPTED, "No user available in the database");
        }
        return users;
    }

    public ResponseEntity<?> deleteUser (Long id) {
        Optional<User> findUser = isUserExist(id);
        ResponseEntity<ResponseMessage> responseMessage1 = userDoesNotExist(id);
        if (findUser.isEmpty()) {
            return responseMessage1;
        }
        String message = String.format("%s with an id %s have been deleted",
                (findUser.get().getFirstName() + " " + findUser.get().getLastName()), id);
        userRepo.delete(findUser.get());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public Optional<User> isUserExist(Long id) {
        return userRepo.findById(id);
    }

    private static ResponseEntity<ResponseMessage> userDoesNotExist(Long id) {
        String message = String.format("No user exist with an id %s", id);
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.NOT_FOUND.name(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
