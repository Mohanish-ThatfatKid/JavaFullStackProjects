package com.mo.service;

import com.mo.model.Property;
import com.mo.model.User;
import com.mo.requestDTO.BookingRequest;

import jakarta.mail.MessagingException;

public interface IBookingService {

	public String createBooking(Long propertyId, String email, BookingRequest request) throws MessagingException;

	public void sendConfirmationEmail(Property property, User user, String bookingId) throws MessagingException;
}
