package com.mo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mo.model.Property;
import java.util.List;


public interface PropertyRepository extends JpaRepository<Property, Long>{

	List<Property> findByBasePriceBetween(double startprice, double maxPrice);
}
