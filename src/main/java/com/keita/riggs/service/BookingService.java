package com.keita.riggs.service;

import com.keita.riggs.handler.ErrorMessage;
import com.keita.riggs.handler.ExceptHandler;
import com.keita.riggs.handler.InvalidInput;
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

    public ResponseEntity<?> save (Booking booking, long userID, long roomID, BindingResult bindingResult, HttpServletResponse servletResponse) {

        if (bindingResult.hasErrors()) {
            return InvalidInput.userError(bindingResult, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        long bookingID = Util.generateID(9999999);
        while (isBookingExist(bookingID).isPresent()) {
            bookingID = Util.generateID(9999999);
        }

        booking.setBookingID(bookingID);
        booking.setBookDate(new Date());

        User user = userService.getUser(userID, servletResponse);
        booking.setUser(user);

        Room room = roomService.getRoom(roomID, servletResponse);
        booking.setRoom(room);

        Booking bookingResult = bookingRepo.save(booking);

        String message = String.format("New booking have been created with an id %s", bookingResult.getBookingID());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public Optional<Booking> findBookingByID (Long id, HttpServletResponse response) {
        String message = "No booking found with an id " + id;
        return bookingRepo.findById(id)
                .map(Optional::of)
                .orElseThrow(() -> new ExceptHandler(HttpStatus.UNPROCESSABLE_ENTITY, response, message));
    }

    public List<Booking> bookingList(HttpServletResponse response) {
        List<Booking> bookings = bookingRepo.getAllBookingr();
        if (bookings.isEmpty()) {
            new ErrorMessage(response, "No bookings available in the database");
            return null;
        }
        return bookings;
    }

    public ResponseEntity<?> deleteBooking (Long id) {
        Optional<Booking> findBooking = isBookingExist(id);
        ResponseEntity<ResponseMessage> responseMessage1 = bookingDoesNotExist(id);
        if (findBooking.isEmpty()) {
            return responseMessage1;
        }
        String message = String.format("Booking with an id %s have been deleted", id);
        bookingRepo.delete(findBooking.get());
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.OK.name(), HttpStatus.OK.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public Optional<Booking> isBookingExist(Long id) {
        return bookingRepo.findById(id);
    }

    private static ResponseEntity<ResponseMessage> bookingDoesNotExist(Long id) {
        String message = String.format("No booking exist with an id %s", id);
        ResponseMessage responseMessage = new ResponseMessage(message, HttpStatus.NOT_FOUND.name(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
