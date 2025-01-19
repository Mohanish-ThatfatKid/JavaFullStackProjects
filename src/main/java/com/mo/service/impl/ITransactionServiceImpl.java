package com.mo.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mo.domain.BookingCurrentStatus;
import com.mo.model.BookingDetails;
import com.mo.model.Property;
import com.mo.model.Transaction;
import com.mo.model.User;
import com.mo.repository.TransactionRepository;
import com.mo.service.ITransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ITransactionServiceImpl implements ITransactionService {
	
	private final TransactionRepository tranRepo;
	
	
	@Override
	public Transaction createTransaction(User user, Property property, BookingDetails bookingDetails) {
	
	Map<BookingCurrentStatus, LocalDate> map = new HashMap<>();
	Transaction transaction = new Transaction();
		transaction.setBookingDetails(bookingDetails);
		transaction.setProperty(property);
		transaction.setUser(user);
		map.put(BookingCurrentStatus.Booked, LocalDate.now());
		transaction.setBookingStatus(map);
		return tranRepo.save(transaction);
	}

	
	
}
