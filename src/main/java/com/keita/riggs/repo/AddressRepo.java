package com.keita.riggs.repo;

import com.keita.riggs.model.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepo extends CrudRepository<Address, Long> {
}
