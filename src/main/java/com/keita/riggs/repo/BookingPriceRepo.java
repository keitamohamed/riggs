package com.keita.riggs.repo;

import com.keita.riggs.model.BookingPrice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookingPriceRepo extends CrudRepository<BookingPrice, Long> {

    @Modifying
    @Query("UPDATE BookingPrice p SET p.price = :price WHERE p.id = :id")
    void setBookingBriceByID(@Param("id") long id, @Param("price") double price);

    @Modifying
    @Transactional
    @Query("DELETE FROM BookingPrice b WHERE b.id = :id")
    void deleteBookingPriceById(@Param("id") long id);
}
