package com.mo.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode
public class Complaints {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true, length = 6)
	private String complaintNumber;
	
	@Column(columnDefinition = "LONGTEXT")
	private String complaintInfo;
	
	private String bookingId;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Property property;
	//property class
	
	@Column(nullable = false)
	@CreationTimestamp
	private Instant createdAt;
	
}
