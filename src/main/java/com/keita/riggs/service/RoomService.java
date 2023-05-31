package com.keita.riggs.service;

import com.keita.riggs.handler.ErrorMessage;
import com.keita.riggs.handler.InvalidInput;
import com.keita.riggs.handler.ExceptHandler;
import com.keita.riggs.mapper.ResponseMessage;
import com.keita.riggs.model.Room;
import com.keita.riggs.repo.RoomDetailRepo;
import com.keita.riggs.repo.RoomRepo;
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
public class RoomService {

    private final RoomRepo roomRepo;
    private final RoomDetailRepo roomDetailRepo;

    @Autowired
    public RoomService(RoomRepo roomRepo, RoomDetailRepo roomDetailRepo) {
        this.roomRepo = roomRepo;
        this.roomDetailRepo = roomDetailRepo;
    }

    public ResponseEntity<?> save (Room room, BindingResult result) {
        if (result.hasErrors()) {
            return InvalidInput.roomError(result, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        long roomID = Util.generateID(9999999);
        while (isRoomExist(roomID).isPresent()) {
            roomID = Util.generateID(9999999);
        }
        room.setRoomID(roomID);
        Room saveResult = roomRepo.save(room);
        String message = String.format("New room have been added with an id %s ", saveResult.getRoomID());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public ResponseEntity<?> updateRoom(Room room, BindingResult bindingResult) {
        Optional<Room> result = isRoomExist(room.getRoomID());
        ResponseEntity<ResponseMessage> responseMessage1 = roomDoesNotExist(room.getRoomID());
        if (result.isEmpty()) {
            return responseMessage1;
        }

        if (bindingResult.hasErrors()) {
            return InvalidInput.roomError(bindingResult, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        roomDetailRepo.save(room.getRoom());

        result.ifPresent(room1 -> {
            room1.setRoomName(room.getRoomName());
            room1.setSize(room.getSize());
            room1.setDescription(room.getDescription());

        });

        Room updated = roomRepo.save(result.get());
        String message = String.format("%s room with an id %s have been updated", updated.getRoomName(), updated.getRoomID());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    private static ResponseEntity<ResponseMessage> roomDoesNotExist(Long id) {
        String message = String.format("No room exist with an id %s", id);
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.NOT_FOUND.name(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public Optional<Room> findRoomByID (Long id, HttpServletResponse response) {
        String message = "No room found with an id " + id;
        return roomRepo.findById(id)
                .map(Optional::of)
                .orElseThrow(() -> new ExceptHandler(HttpStatus.UNPROCESSABLE_ENTITY, response, message));
    }

    public List<Room> list(HttpServletResponse response) {
        List<Room> rooms = roomRepo.getAllRoom();
        if (rooms.isEmpty()) {
            throw new ErrorMessage(response, HttpStatus.ACCEPTED, "No data available in the database");
        }
        return rooms;
    }
    public Optional<Room> isRoomExist(Long id) {
        return roomRepo.findById(id);
    }

    public ResponseEntity<?> deleteRoom (Long id) {
        Optional<Room> findRoom = isRoomExist(id);
        ResponseEntity<ResponseMessage> responseMessage1 = roomDoesNotExist(id);
        if (findRoom.isEmpty()) {
            return responseMessage1;
        }
        String message = String.format("%s with an id %s have been deleted", findRoom.get().getRoomName(), id);
        roomRepo.delete(findRoom.get());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }


}
