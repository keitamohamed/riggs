package com.keita.riggs.repo;

import com.keita.riggs.model.ImagePath;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageRepo extends CrudRepository<ImagePath, Long> {

    @Transactional
    @Query(value = "SELECT * FROM image_path AS img WHERE img.roomID = :roomID", nativeQuery = true)
    List<ImagePath> findImagePathByRoomID(@Param("roomID") long roomID);
}
