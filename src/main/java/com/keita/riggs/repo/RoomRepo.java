package com.keita.riggs.repo;

import com.keita.riggs.model.Room;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RoomRepo extends CrudRepository<Room, Long> {

    @NotNull
    Room save(Room room);

    @Transactional
    @Query(value = "SELECT * FROM room", nativeQuery = true)
    List<Room> getAllRoom();
}
