package com.keita.riggs.service;

import com.keita.riggs.repo.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingRepo bookingRepo;

    @Autowired
    public BookingService(BookingRepo bookingRepo) {
        this.bookingRepo = bookingRepo;
    }


}
