package com.keita.riggs.repo;

import com.keita.riggs.model.Authenticate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AuthenticateRepo extends CrudRepository<Authenticate, Long> {

    Authenticate findByEmail(String email);


//    @Query(
//            value = "SELECT * " +
//                    "FROM authenticate as a " +
//                    "WHERE a.email = :email", nativeQuery = true
//    )
//    Authenticate findUserByEmail(@Param("email") String email);


}
