package com.mo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mo.model.HostUser;
import com.mo.model.Property;

public interface PropertyRepository extends JpaRepository<Property, Long>,JpaSpecificationExecutor<Property> {

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
	List<Property> findAvailableProperties(@Param("checkInDate") LocalDate checkInDate,
			@Param("checkOutDate") LocalDate checkOutDate);

	List<Property> findByHost(HostUser host);

	@Query("SELECT p FROM Property p JOIN p.reviews r GROUP BY p HAVING AVG(r.rating) >= :minRating")
	List<Property> findPropertiesByMinRating(double minRating);
	
	 @Query("""
		        SELECT p 
		        FROM Property p 
		        WHERE (:pickUpDrop IS NULL OR p.amenities.pickUpDrop = :pickUpDrop) 
		          AND (:wifi IS NULL OR p.amenities.wifi = :wifi) 
		          AND (:swimmingPool IS NULL OR p.amenities.swimmingPool = :swimmingPool) 
		          AND (:freeBreakfast IS NULL OR p.amenities.freeBreakfast = :freeBreakfast) 
		          AND (:garden IS NULL OR p.amenities.garden = :garden) 
		          AND (:playArea IS NULL OR p.amenities.playArea = :playArea)
		    """)
		    List<Property> findPropertiesByAmenities(
		        @Param("pickUpDrop") Boolean pickUpDrop,
		        @Param("wifi") Boolean wifi,
		        @Param("swimmingPool") Boolean swimmingPool,
		        @Param("freeBreakfast") Boolean freeBreakfast,
		        @Param("garden") Boolean garden,
		        @Param("playArea") Boolean playArea
		    );
	 
	 List<Property> findByMaxGuests(int maxGuests);
	 List<Property> findByActive(boolean active);

}
