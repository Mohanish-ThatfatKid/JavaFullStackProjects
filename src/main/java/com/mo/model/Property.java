package com.mo.model;

import java.util.ArrayList;
import java.util.List;

import com.mo.domain.PropertyLocationCategory;
import com.mo.domain.PropertySizeCategory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode
public class Property {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@Column(columnDefinition = "LONGTEXT")
	private String description;
	
	@Enumerated(EnumType.STRING)
	private PropertyLocationCategory propertyLocationCategory;
	
	@Enumerated(EnumType.STRING)
	private PropertySizeCategory propertySizeCategory;
	
	@ElementCollection
	private List<String> images = new ArrayList<>();
	
	@ManyToOne
	private HostUser host;
	
	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews = new ArrayList<>();
	
	@Embedded
	private Amenities amenities = new Amenities();
	
	private double basePrice;
	
	private double offerPrice;
	
	@OneToOne
	private Address address;
	
	private int maxGuests;
	
	private boolean isActive;
	
	private String contactDetails;
	
	private String contactEmail;
	
	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookingDetails> bookings = new ArrayList<>();
	
	@OneToMany(mappedBy = "property")
	private List<Complaints> complaints = new ArrayList<>();
	
	
	
}


