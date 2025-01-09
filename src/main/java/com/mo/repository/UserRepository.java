package com.mo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mo.model.User;



public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);
	User findByAadharNumber(String aadharNumber);
	
}
