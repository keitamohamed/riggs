package com.keita.riggs.service;

import com.keita.riggs.exception.NotFoundException;
import com.keita.riggs.exception.UnprocessableDataException;
import com.keita.riggs.mapper.ResponseMessage;
import com.keita.riggs.model.Booking;
import com.keita.riggs.model.Room;
import com.keita.riggs.model.User;
import com.keita.riggs.repo.BookingRepo;
import com.keita.riggs.util.Util;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepo bookingRepo;
    private final RoomService roomService;
    private final UserService userService;

    @Autowired
    public BookingService(BookingRepo bookingRepo, RoomService roomService, UserService userService) {
        this.bookingRepo = bookingRepo;
        this.roomService = roomService;
        this.userService = userService;
    }

    public ResponseEntity<?> save (Booking booking, BindingResult bindingResult, HttpServletResponse servletResponse) {

        if (bindingResult.hasErrors()) {
            throw new UnprocessableDataException("Unable to Register User", bindingResult);
        }

        long bookingID = Util.generateID(9999999);
        while (isBookingExist(bookingID).isPresent()) {
            bookingID = Util.generateID(9999999);
        }

        booking.setBookingID(bookingID);
        booking.setBookDate(new Date());

        User user = userService.getUser(booking.getUser().getUserID());
        booking.setUser(user);

        Room findRoom;
        int index = 0;
        List<Room> roomList = new ArrayList<>();

        while (booking.getRooms().size() > index) {
            Room r = booking.getRooms().get(index);
            findRoom = roomService.getRoom(r.getRoomID());
            roomList.add(findRoom);
            booking.getPrices().get(index).setBookingPrice(r.getPrice());
            index++;
        }

        booking.setRooms(roomList);
        Booking bookingResult = bookingRepo.save(booking);

        roomList.forEach(r -> roomService.updateBooking(r, bookingResult));

        String message = String.format("New booking have been created #%s", bookingResult.getBookingID());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public Optional<Booking> findBookingByID (Long id, HttpServletResponse response) {
        return bookingRepo.findById(id)
                .map(Optional::of)
                .orElseThrow(() -> new NotFoundException("No booking found with an id " + id));
    }

    public List<Booking> bookingList() {
        List<Booking> bookings = bookingRepo.getAllBookingr();
        if (bookings.isEmpty()) {
            throw new NotFoundException("No bookings available in the database");
        }
        return bookings;
    }

    public ResponseEntity<?> deleteBooking (Long id) {
        Optional<Booking> findBooking = isBookingExist(id);
        findBooking.orElseThrow(() -> new NotFoundException("No booking exist with an id " + id));

        String message = String.format("Booking #%s deleted successfully", id);
        Booking booking = findBooking.get();

        int index = 0;
        List<Room> roomList = booking.getRooms();
        while (roomList.size() > index) {
            roomService.updateBooking(roomList.get(index), null);
            index++;
        }
        bookingRepo.deleteBookingByBookingID(booking.getBookingID());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public Optional<Booking> isBookingExist(Long id) {
        return bookingRepo.findById(id);
    }

}
