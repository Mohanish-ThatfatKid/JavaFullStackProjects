package com.mo.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mo.exceptions.PropertyException;
import com.mo.exceptions.PropertyRegistrationException;
import com.mo.model.Address;
import com.mo.model.HostUser;
import com.mo.model.Property;
import com.mo.repository.AddressRepository;
import com.mo.repository.PropertyRepository;
import com.mo.service.IPropertyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IPropertyServiceImpl implements IPropertyService {

	private final PropertyRepository propRepo;
	private final AddressRepository addressRepo;

	@Override
	public List<Property> getAllProperties(int pagenumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Property getPropertyById(Long id) {

		return propRepo.findById(id).orElseThrow(() -> new PropertyException("No property found on provided id"));
	}

	@Override
	public List<Property> findPropertiesByAmenities(Boolean pickUpDrop, Boolean wifi, Boolean swimmingPool,
			Boolean freeBreakfast, Boolean garden, Boolean playArea) {
		return propRepo.findPropertiesByAmenities(pickUpDrop, wifi, swimmingPool, freeBreakfast, garden, playArea);
	}

	@Override
	public List<Property> getPropertiesByRating(double rating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Property> getPropertiesByPrice(double minPrice, double MaxPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Property> getPropertiesByHost(HostUser host) {
		return propRepo.findByHost(host);
	}

	@Override
	public List<Property> getPropertiesByDateAvailable(LocalDate startDate, LocalDate endDate) {
		return propRepo.findAvailableProperties(startDate, endDate);
	}

	@Override
	public Property createProperty(Property property, HostUser host) {

		property.setHost(host);
		property.setActive(true);

		Address address = addressRepo.save(property.getAddress());
		if (address == null) {
			throw new PropertyRegistrationException("Failed to save address while adding the property");
		}
		property.setAddress(address);

		Property savedProperty = propRepo.save(property);

		if (savedProperty == null) {

			throw new PropertyRegistrationException("Property failed to register");
		}

		return savedProperty;
	}

	@Override
	public Property updateProperty(Long id, Property property, HostUser host) {

		return null;
	}

	@Override
	public void deleteProperty(Long id, HostUser host) {
		propRepo.deleteById(id);
	}

}
