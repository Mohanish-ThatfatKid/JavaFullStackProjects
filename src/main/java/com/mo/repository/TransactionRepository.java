package com.mo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mo.model.HostUser;
import com.mo.model.Property;
import com.mo.model.Transaction;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByHost(HostUser host);
	List<Transaction> findByProperty(Property property);
	
	
}
