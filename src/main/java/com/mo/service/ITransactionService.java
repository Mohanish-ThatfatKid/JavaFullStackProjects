package com.mo.service;

import com.mo.model.BookingDetails;
import com.mo.model.Property;
import com.mo.model.Transaction;
import com.mo.model.User;

public interface ITransactionService {

	public Transaction createTransaction(User user, Property property, BookingDetails bookingDetails);
	
}
