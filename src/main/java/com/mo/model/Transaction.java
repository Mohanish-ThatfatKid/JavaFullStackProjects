package com.mo.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.mo.domain.BookingCurrentStatus;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Property property;
	
	@ManyToOne
	private BookingDetails bookingDetails;
	
	private double totalPrice;
	
	@Enumerated
	@ElementCollection
	private Map<BookingCurrentStatus, LocalDate> bookingStatus = new HashMap<>();
}
