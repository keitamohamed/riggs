package com.keita.riggs.repo;

import com.keita.riggs.model.Authenticate;
import org.springframework.data.repository.CrudRepository;

public interface AuthenticateRepo extends CrudRepository<Authenticate, Long> {

    Authenticate findByEmail(String email);
}
