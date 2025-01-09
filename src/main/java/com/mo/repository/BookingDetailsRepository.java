package com.mo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mo.model.BookingDetails;
import com.mo.model.Property;
import com.mo.model.User;

import java.util.List;


public interface BookingDetailsRepository extends JpaRepository<BookingDetails, Long>{
	
	public BookingDetails findByBookingId(String bookingId);
	
	List<BookingDetails> findByUser(User user);
	
	List<BookingDetails> findByProperty(Property property);
	
}
