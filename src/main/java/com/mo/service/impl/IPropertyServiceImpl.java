package com.mo.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mo.domain.PropertyLocationCategory;
import com.mo.domain.PropertySizeCategory;
import com.mo.exceptions.PropertyException;
import com.mo.exceptions.PropertyRegistrationException;
import com.mo.model.Address;
import com.mo.model.HostUser;
import com.mo.model.Property;
import com.mo.repository.AddressRepository;
import com.mo.repository.PropertyRepository;
import com.mo.service.IPropertyService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IPropertyServiceImpl implements IPropertyService {

	private final PropertyRepository propRepo;
	private final AddressRepository addressRepo;

	@Override
	public Page<Property> getAllProperties(String propertyLocationCategory, String propertySizeCategory,
			Double minimumBasePrice, Double maximumBasePrice, Double minimumOfferPrice, Double maximumOfferPrice,
			Integer maxGuests, Boolean isActive, String sort, Integer pageNumber) {
		Specification<Property> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (propertyLocationCategory != null) {
				predicates.add(criteriaBuilder.equal(root.get("propertyLocationCategory"),
						PropertyLocationCategory.valueOf(propertyLocationCategory)));
			}
			if (propertySizeCategory != null) {
				predicates.add(criteriaBuilder.equal(root.get("propertySizeCategory"),
						PropertySizeCategory.valueOf(propertySizeCategory)));
			}
			if (minimumBasePrice != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("basePrice"), minimumBasePrice));
			}
			if (maximumBasePrice != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("basePrice"), maximumBasePrice));
			}
			if (minimumOfferPrice != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("offerPrice"), minimumOfferPrice));
			}
			if (maximumOfferPrice != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("offerPrice"), maximumOfferPrice));
			}
			if (maxGuests != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("maxGuests"), maxGuests));
			}
			if (isActive != null) {
				predicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};

		Pageable pageable;
		if (sort != null) {
			pageable = switch (sort) {
			case "price_low" ->
				PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("basePrice").ascending());
			case "price_high" ->
				PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("basePrice").descending());
			case "offer_price_low" ->
				PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("offerPrice").ascending());
			case "offer_price_high" ->
				PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.by("offerPrice").descending());
			default -> PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
			};
		} else {
			pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
		}

		return propRepo.findAll(specification, pageable);
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
		return propRepo.findPropertiesByMinRating(rating);
	}

	@Override
	public List<Property> getPropertiesByPrice(double minPrice, double MaxPrice) {
		return propRepo.findByBasePriceBetween(minPrice, MaxPrice);
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
