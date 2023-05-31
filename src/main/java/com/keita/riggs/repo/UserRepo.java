package com.keita.riggs.repo;

import com.keita.riggs.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepo extends CrudRepository<User, Long> {

    @Transactional
    @Query(value = "SELECT * FROM user", nativeQuery = true)
    List<User> getAllUser();
}
