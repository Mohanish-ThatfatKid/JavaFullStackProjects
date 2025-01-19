package com.mo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mo.requestDTO.BookingRequest;
import com.mo.service.IBookingService;
import com.mo.service.IUserService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
 
	
	private final IBookingService bookingService;
	private final IUserService userService;
	
	private final String authorizationHeader = "Authorization";
	
	@PostMapping("/user/property/{id}")
	public ResponseEntity<String> bookingHandler(@RequestHeader(authorizationHeader) String jwt, @RequestBody BookingRequest request, @PathVariable Long id) throws MessagingException{
		
		String email = userService.getuserEmailFromJWt(jwt);
		String booking = bookingService.createBooking(id, email, request);
		
		return ResponseEntity.ok(booking);
	}
	
}
