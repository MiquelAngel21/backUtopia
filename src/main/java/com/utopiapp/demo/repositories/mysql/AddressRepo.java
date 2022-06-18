package com.utopiapp.demo.repositories.mysql;

import com.utopiapp.demo.model.Address;
import com.utopiapp.demo.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
    Address findAddressByClub_id(Long clubId);
}
