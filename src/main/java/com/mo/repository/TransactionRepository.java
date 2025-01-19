package com.mo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mo.model.Property;
import com.mo.model.Transaction;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByProperty(Property property);
	
	
}
