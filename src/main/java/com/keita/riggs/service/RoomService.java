package com.keita.riggs.service;

import com.keita.riggs.exception.NotFoundException;
import com.keita.riggs.exception.UnprocessableRoomDataException;
import com.keita.riggs.mapper.ResponseMessage;
import com.keita.riggs.model.Booking;
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

    public ResponseEntity<?> save (Room room, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UnprocessableRoomDataException("Unable to add new room", bindingResult);
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
        Optional<Room> getRoom = isRoomExist(room.getRoomID());
        getRoom.orElseThrow(() -> new NotFoundException(String.format("No room exist with an id %s", room.getRoomID())));

        if (bindingResult.hasErrors()) {
            throw new UnprocessableRoomDataException("Unable to update room information", bindingResult);
        }

        roomDetailRepo.save(room.getDetail());

        getRoom.ifPresent(room1 -> {
            room1.setRoomName(room.getRoomName());
            room1.setSize(room.getSize());
            room1.setDescription(room.getDescription());

        });

        Room updated = roomRepo.save(getRoom.get());
        String message = String.format("%s room with an id %s have been updated", updated.getRoomName(), updated.getRoomID());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    private static ResponseEntity<ResponseMessage> roomDoesNotExist(Long id) {
        String message = String.format("No room exist with an id %s", id);
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.NOT_FOUND.name(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public Optional<Room> findRoomByID (Long id) {
        return roomRepo.findById(id)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("No room found with an id " + id));
    }

    public List<Room> list(HttpServletResponse response) {
        List<Room> rooms = roomRepo.getAllRoom();
        if (rooms.isEmpty()) {
            throw new NotFoundException("No data available in the database");
        }
        return rooms;
    }
    private Optional<Room> isRoomExist(Long id) {
        return roomRepo.findById(id);
    }

    public Room getRoom(Long id) {
        Optional<Room> findRoom = isRoomExist(id);
        return findRoom.orElseThrow(() -> new NotFoundException(String.format("No room exist with an id %s ", id)));
    }

    public Room updateBooking(Room room, Booking booking) {
        room.setRooms(booking);
        return roomRepo.save(room);
    }

    public ResponseEntity<?> deleteRoom (Long id) {
        Optional<Room> findRoom = isRoomExist(id);
        findRoom.orElseThrow(() -> new NotFoundException(String.format("No room exist with an id %s", id)));

        String message = String.format("%s with an id %s have been deleted", findRoom.get().getRoomName(), id);
        roomRepo.delete(findRoom.get());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
