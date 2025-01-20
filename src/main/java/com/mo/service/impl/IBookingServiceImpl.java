
package com.mo.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mo.exceptions.BookingDetailsException;
import com.mo.model.BookingDetails;
import com.mo.model.HostUser;
import com.mo.model.Property;
import com.mo.model.User;
import com.mo.repository.BookingDetailsRepository;
import com.mo.requestDTO.BookingRequest;
import com.mo.service.IBookingService;
import com.mo.service.IEmailService;
import com.mo.service.IHostUserService;
import com.mo.service.IPropertyService;
import com.mo.service.IUserService;
import com.mo.utils.HelperUtil;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IBookingServiceImpl implements IBookingService {


	private final BookingDetailsRepository bookingRepo;
	private final IPropertyService propService;
	private final IUserService UserServise;
	private final IEmailService emailService;
	private final IHostUserService hostService;

	@Override
	@Transactional
	public String createBooking(Long propertyId, String email, BookingRequest request) throws MessagingException {
		String bookingId = HelperUtil.generateBookingId();
		Property property = propService.getPropertyById(propertyId);
		User user = UserServise.getUserByEmail(email);

		LocalDate checkInDate = LocalDate.parse(request.getCheckInDate());
		LocalDate checkOutDate = LocalDate.parse(request.getCheckOutDate());

		List<BookingDetails> overlappingBookings = bookingRepo.findOverlappingBookingsWithLock(propertyId, checkInDate,
				checkOutDate);

		if (!overlappingBookings.isEmpty()) {
			throw new IllegalArgumentException("Property is already booked for the selected dates.");
		}

		BookingDetails details = new BookingDetails();
		details.setBookingId(bookingId);
		details.setCheckInDate(checkInDate);
		details.setCheckOutDate(checkOutDate);
		details.setProperty(property);
		details.setUser(user);
		details.setTotalPrice(request.getTotalPrice());
		property.getBookings().add(details);

		BookingDetails createdBooking = bookingRepo.save(details);
		if (createdBooking == null) {
			throw new BookingDetailsException("Failed to book the property please try again.");
		}

		sendConfirmationEmail(property, user, bookingId);

		return bookingId;
	}

	@Override
	public void sendConfirmationEmail(Property property, User user, String bookingId) throws MessagingException {
		String textToUser = "Dear, " + user.getFirstName() + " thank you for choosing " + property.getName()
				+ " as your Stakation home. \n"
				+ "We at stakation are please to serve you at your stay. /nHappy Stakation";

		String subject = "Booking confirmation. BookingId: " + bookingId;

		emailService.sendBookingConfirmedEmail(user.getEmail(), textToUser, subject);

		String textToHost = "Hello, " + property.getName()
				+ " we are happy to inform you that your property have received a booking from" + user.getFirstName()
				+ " " + user.getLastName() + "we are sure that you will serve our guest well./n"
				+ "you can check the details about booking by login in to your account./n Happy Stakation";

		emailService.sendConfirmBookingMailToHost(property.getHost().getEmail(), property.getContactEmail(), textToHost,
				subject);

	}

	@Override
	public List<BookingDetails> getAllBookingsByProperty(Long propertyId, String hostEmail) {
		Property property = propService.getPropertyById(propertyId);
		HostUser host = hostService.getHostUserByEmail(hostEmail);
		List<BookingDetails> bookings = new ArrayList<>();
		if (property.getHost().getId() == host.getId()) {
			bookings = bookingRepo.findByProperty(property);
			return bookings;
		}
		return null;
	}

	@Override
	public boolean cancelBooking(String userEmail, Long id) {
		
		BookingDetails bookingDetails = bookingRepo.findById(id).get();
		if (bookingDetails!=null) {
			User user = UserServise.getUserByEmail(userEmail);
			if (bookingDetails.getUser().getId() == user.getId()) {
				bookingRepo.delete(bookingDetails);
				return true;
			}
		}
		return false;
	}

}
