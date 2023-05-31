package com.keita.riggs.repo;

import com.keita.riggs.model.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookingRepo extends CrudRepository<Booking, Long> {

    @Transactional
    @Query(value = "SELECT * FROM booking", nativeQuery = true)
    List<Booking> getAllBookingr();
}
