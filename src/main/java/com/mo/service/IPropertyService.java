package com.mo.service;

import java.time.LocalDate;
import java.util.List;

import com.mo.model.HostUser;
import com.mo.model.Property;

public interface IPropertyService {

	public List<Property> getAllProperties(int pagenumber);
	
	public Property getPropertyById(Long id);
	
	public List<Property> findPropertiesByAmenities(Boolean pickUpDrop, Boolean wifi, Boolean swimmingPool, Boolean freeBreakfast, Boolean garden, Boolean playArea);
	
	public List<Property> getPropertiesByRating(double rating);
	
	public List<Property> getPropertiesByPrice(double minPrice, double MaxPrice);
	
	public List<Property> getPropertiesByHost(HostUser host);
	
	public List<Property> getPropertiesByDateAvailable(LocalDate startDate, LocalDate endDate);
	
	public Property createProperty(Property property, HostUser host);
	
	public Property updateProperty(Long id, Property property, HostUser host);
	
	public void deleteProperty(Long id, HostUser host);
	
}
