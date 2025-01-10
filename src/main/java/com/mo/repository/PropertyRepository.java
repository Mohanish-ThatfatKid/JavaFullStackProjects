package com.mo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mo.model.Property;


public interface PropertyRepository extends JpaRepository<Property, Long>{

	List<Property> findByBasePriceBetween(double startprice, double maxPrice);
	
	@Query("""
		    SELECT p 
		    FROM Property p 
		    WHERE p.isActive = true 
		    AND NOT EXISTS (
		        SELECT b 
		        FROM BookingDetails b 
		        WHERE b.property = p 
		        AND b.checkInDate <= :checkOutDate 
		        AND b.checkOutDate >= :checkInDate
		    )
		""")
		List<Property> findAvailableProperties(@Param("checkInDate") LocalDate checkInDate, @Param("checkOutDate") LocalDate checkOutDate);

}
