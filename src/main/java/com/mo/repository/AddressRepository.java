package com.mo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mo.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}
