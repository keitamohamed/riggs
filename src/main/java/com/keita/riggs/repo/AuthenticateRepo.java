package com.keita.riggs.repo;

import com.keita.riggs.model.Authenticate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthenticateRepo extends CrudRepository<Authenticate, Long> {

    Authenticate findByEmail(String email);

    @Transactional
    @Query(value = "SELECT * FROM authenticate AS auth WHERE auth.email = :email", nativeQuery = true)
    Optional<Authenticate> findAuthenticateByEmail(@Param("email") String email);

}
