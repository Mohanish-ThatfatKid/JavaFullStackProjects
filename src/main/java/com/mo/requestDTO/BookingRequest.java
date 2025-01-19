package com.mo.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {

	private double totalPrice;
	
	private String checkInDate;
	private String checkOutDate;
	
	
}
