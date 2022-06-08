package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {

}
