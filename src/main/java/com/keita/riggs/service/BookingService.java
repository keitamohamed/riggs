package com.keita.riggs.service;

import com.keita.riggs.handler.ErrorMessage;
import com.keita.riggs.handler.InvalidInput;
import com.keita.riggs.handler.ExceptHandler;
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

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepo bookingRepo;

    @Autowired
    public BookingService(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    public ResponseEntity<?> save (Booking booking, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return InvalidInput.userError(bindingResult, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        long bookingID = Util.generateID(9999999);
        while (isBookingExist(bookingID).isPresent()) {
            bookingID = Util.generateID(9999999);
        }

        booking.setBookingID(bookingID);
        User user = booking.getUser();
        user.setRoomBook(booking);

        Room room = booking.getRoom();
        room.setBooking(booking);

        String message = String.format("New booking have been created with an id %s", booking.getBookingID());
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
            throw new ErrorMessage(response, HttpStatus.ACCEPTED, "No bookings available in the database");
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
