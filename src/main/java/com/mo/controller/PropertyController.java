package com.mo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mo.model.Property;
import com.mo.service.IPropertyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/property")
public class PropertyController {

	private final IPropertyService propService;

	@GetMapping("/search-by-amenities")
	public List<Property> getPropertiesByAmenities(@RequestParam(required = false) Boolean pickUpDrop,
			@RequestParam(required = false) Boolean wifi, @RequestParam(required = false) Boolean swimmingPool,
			@RequestParam(required = false) Boolean freeBreakfast, @RequestParam(required = false) Boolean garden,
			@RequestParam(required = false) Boolean playArea) {

		return propService.findPropertiesByAmenities(pickUpDrop, wifi, swimmingPool, freeBreakfast, garden, playArea);
	}

	@GetMapping("/search-by-price")
	public List<Property> getPropertiesByPriceRange(@RequestParam double startPrice, @RequestParam double maxPrice) {
		return propService.getPropertiesByPrice(startPrice, maxPrice);
	}

	@GetMapping
	public ResponseEntity<Page<Property>> getAllProperties(
			@RequestParam(required = false) String propertyLocationCategory,
			@RequestParam(required = false) String propertySizeCategory,
			@RequestParam(required = false) Double minimumBasePrice,
			@RequestParam(required = false) Double maximumBasePrice,
			@RequestParam(required = false) Double minimumOfferPrice,
			@RequestParam(required = false) Double maximumOfferPrice, @RequestParam(required = false) Integer maxGuests,
			@RequestParam(required = false) Boolean isActive, @RequestParam(required = false) String sort,
			@RequestParam(required = false, defaultValue = "0") Integer pageNumber) {
		Page<Property> properties = propService.getAllProperties(propertyLocationCategory, propertySizeCategory,
				minimumBasePrice, maximumBasePrice, minimumOfferPrice, maximumOfferPrice, maxGuests, isActive, sort,
				pageNumber);
		return ResponseEntity.ok(properties);
	}

	public ResponseEntity<List<Property>> getAllAvailablePropertiesByDateHandler(@RequestParam LocalDate checkInDate,
			@RequestParam LocalDate checkOutDate) {

		List<Property> available = propService.getPropertiesByDateAvailable(checkInDate, checkOutDate);

		return ResponseEntity.ok(available);
	}

}
