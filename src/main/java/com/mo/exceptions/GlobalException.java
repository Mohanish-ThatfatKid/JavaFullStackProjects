package com.mo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(ComplaintsException.class)
	public ResponseEntity<ErrorDetails> complaintsExceptionHandler(ComplaintsException ce, WebRequest req){
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setError(ce.getMessage());
		errorDetails.setDetails(req.getDescription(false));
		
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(BookingDetailsException.class)
	public ResponseEntity<ErrorDetails> bookingDetailsExceptionHandler(BookingDetailsException bc, WebRequest req){
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setError(bc.getMessage());
		errorDetails.setDetails(req.getDescription(false));
		
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	
}
