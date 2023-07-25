package com.keita.riggs.repo;

import com.keita.riggs.model.Booking;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookingRepo extends CrudRepository<Booking, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Booking b WHERE b.bookingID = :id")
    void deleteBookingByBookingID(@Param("id") long id);
    @Transactional
    @Query(value = "SELECT * FROM booking", nativeQuery = true)
    List<Booking> getAllBookingr();
}
