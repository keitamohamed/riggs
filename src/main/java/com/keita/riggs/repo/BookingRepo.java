package com.keita.riggs.repo;

import com.keita.riggs.model.Booking;
import org.springframework.data.repository.CrudRepository;

import java.awt.print.Book;

public interface BookingRepo extends CrudRepository<Booking, Long> {
}
