package com.mo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mo.model.Property;
import com.mo.service.IPropertyService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class PropertyController {

	private final IPropertyService propService;
	
	@GetMapping("/search-by-amenities")
    public List<Property> getPropertiesByAmenities(
            @RequestParam(required = false) Boolean pickUpDrop,
            @RequestParam(required = false) Boolean wifi,
            @RequestParam(required = false) Boolean swimmingPool,
            @RequestParam(required = false) Boolean freeBreakfast,
            @RequestParam(required = false) Boolean garden,
            @RequestParam(required = false) Boolean playArea) {

        return propService.findPropertiesByAmenities(pickUpDrop, wifi, swimmingPool, freeBreakfast, garden, playArea);
    }
	
	@GetMapping("/search-by-price")
    public List<Property> getPropertiesByPriceRange(
            @RequestParam double startPrice,
            @RequestParam double maxPrice) {
        return propService.getPropertiesByPrice(startPrice, maxPrice);
    }
	
}
