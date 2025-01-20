package com.mo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mo.model.BookingDetails;
import com.mo.requestDTO.BookingRequest;
import com.mo.response.ApiResponse;
import com.mo.service.IBookingService;
import com.mo.service.IHostUserService;
import com.mo.service.IUserService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

	private final IBookingService bookingService;
	private final IUserService userService;
	private final IHostUserService hostService;

	private final String authorizationHeader = "Authorization";

	@PostMapping("/user/property/{id}")
	public ResponseEntity<String> bookingHandler(@RequestHeader(authorizationHeader) String jwt,
			@RequestBody BookingRequest request, @PathVariable Long id) throws MessagingException {

		String email = userService.getuserEmailFromJWt(jwt);
		String booking = bookingService.createBooking(id, email, request);

		return ResponseEntity.ok(booking);
	}

	@GetMapping("/host/property/{id}")
	public ResponseEntity<List<BookingDetails>> getAllBookingsByProperty(@RequestHeader(authorizationHeader) String jwt,
			@PathVariable Long id) {
		String hostEmail = hostService.getHostUserEmailFromJwt(jwt);
		List<BookingDetails> bookingsByProperty = bookingService.getAllBookingsByProperty(id, hostEmail);
		return ResponseEntity.ok(bookingsByProperty);
	}

	@PostMapping("/user/cancel-booking/{id}")
	public ResponseEntity<ApiResponse> cancelBookingHandler(@RequestHeader(authorizationHeader) String jwt,
			@PathVariable Long id) {
		
		String email = userService.getuserEmailFromJWt(jwt);
		boolean flag = bookingService.cancelBooking(email, id);
		if (flag) {
			return new ResponseEntity<ApiResponse>(new ApiResponse("Booking Cancelled Successfully"), HttpStatus.OK);
		}
		
		return new ResponseEntity<ApiResponse>(new ApiResponse("Failed to cancel the booking"), HttpStatus.BAD_REQUEST);
	}

}
