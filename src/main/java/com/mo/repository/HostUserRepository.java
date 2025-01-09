package com.mo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mo.model.HostUser;

public interface HostUserRepository extends JpaRepository<HostUser, Long> {

	public HostUser findByEmail(String email);
	
	public HostUser findByAadharNumber(String aadharNumber);
	
}
