package com.keita.riggs.repo;

import com.keita.riggs.model.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepo extends CrudRepository<Room, Long> {
}
