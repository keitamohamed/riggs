package com.keita.riggs.repo;

import com.keita.riggs.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
}
