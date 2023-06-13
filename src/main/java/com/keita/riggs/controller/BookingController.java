package com.keita.riggs.controller;

import com.keita.riggs.model.Booking;
import com.keita.riggs.service.BookingService;
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
@RequestMapping("/riggs/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(
            path = {"/add/{userID}/{roomID}"},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> newBooking(
            @PathVariable long userID,
            @PathVariable long roomID,
            @Valid
            @RequestBody
            Booking booking,
            BindingResult bindingResult,
            HttpServletResponse servletResponse
    ) {
        return bookingService.save(booking, userID, roomID, bindingResult, servletResponse);
    }

    @GetMapping(path = {"/find-by-id/{bookingID}"})
    public Optional<Booking> findBookingByID(
            @PathVariable
            Long bookingID,
            HttpServletResponse servletResponse
    ) {
        return bookingService.findBookingByID(bookingID, servletResponse);
    }

    @GetMapping(path = {"/list-of-booking"})
    public List<Booking> bookingList(HttpServletResponse servletResponse) {
        return bookingService.bookingList(servletResponse);
    }

    @DeleteMapping(
            path = {"/delete-booking/{bookingID}"}
    )
    public ResponseEntity<?> deleteBooking(
            @PathVariable
            Long bookingID
    ) {
        return bookingService.deleteBooking(bookingID);
    }
}
