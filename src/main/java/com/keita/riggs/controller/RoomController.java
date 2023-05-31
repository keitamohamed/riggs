package com.keita.riggs.controller;

import com.keita.riggs.model.Room;
import com.keita.riggs.service.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/riggs/room")
public class RoomController {

    private final RoomService service;

    @Autowired
    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping(
            value = {"/add"},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addRoom (
            @Valid
            @RequestBody
            Room room,
            BindingResult result) {
        return service.save(room, result);
    }

    @PutMapping(
            path = {"/update/{id}"},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateRoom(
            @Valid
            @RequestBody
            Room room,
            BindingResult bindingResult
    )
    {
        return service.updateRoom(room, bindingResult);

    }

    @GetMapping(path = {"/list"})
    public List<Room> roomList(HttpServletResponse response) {
        return service.list(response);
    }

    @GetMapping(value = {"/find-by-id/{id}"})
    public Optional<Room> findRoomByID(@PathVariable Long id, HttpServletResponse response) {
        return service.findRoomByID(id, response);
    }

    @DeleteMapping(
            path = {"/delete/{id}"}
    )
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        return service.deleteRoom(id);
    }

}
