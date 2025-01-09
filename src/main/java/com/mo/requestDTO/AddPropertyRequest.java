package com.mo.requestDTO;

import java.util.List;

import com.mo.domain.PropertyLocationCategory;
import com.mo.domain.PropertySizeCategory;

import lombok.Data;

@Data
public class AddPropertyRequest {

	private String name;

	private String description;

	private PropertyLocationCategory propertyLocationCategory;

	private PropertySizeCategory propertySizeCategory;

	private List<String> images;

	private boolean pickUpDrop;
	private boolean wifi;
	private boolean swimmingPool;
	private boolean freeBreakfast;
	private boolean garden;
	private boolean playArea;

	private double basePrice;

	private double offerPrice;

	private String locality;

	private String city;

	private String state;

	private String pincode;

	private int maxGuests;

	private boolean isActive;

	private String contactDetails;

	private String contactEmail;

}
