package com.keita.riggs.repo;

import com.keita.riggs.model.Hotel;
import org.springframework.data.repository.CrudRepository;

public interface HotelRepo extends CrudRepository<Hotel, Long> {
}
