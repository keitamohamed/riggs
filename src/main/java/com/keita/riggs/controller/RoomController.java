package com.keita.riggs.controller;

import com.keita.riggs.model.Room;
import com.keita.riggs.service.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/riggs/room")
public class RoomController {

    private final RoomService service;
    private final String DIR_PATH = "src/main/resources/image/";

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
    ) {
        return service.updateRoom(room, bindingResult);

    }

    @GetMapping(path = {"/list"})
    public List<Room> roomList(HttpServletResponse response) {
        ClassPathResource res = new ClassPathResource("images");
        return service.list(response);
    }

    @GetMapping(value = {"/find-by-id/{id}"})
    public Optional<Room> findRoomByID(@PathVariable Long id) {
        return service.findRoomByID(id);
    }

    @DeleteMapping(
            path = {"/delete/{id}"}
    )
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        return service.deleteRoom(id);
    }

    @PostMapping(value = {"/save-image/{id}"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> saveFile (
            @PathVariable long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        return service.saveImage(file, id);
    }

    @GetMapping(
            value = {"/download-image/{id}"},
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE}
    )
    public byte[] getRoomImage(@PathVariable long id) throws IOException {
        return service.roomImage(id);
    }

}
