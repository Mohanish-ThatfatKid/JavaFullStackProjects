package com.mo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mo.model.BookingDetails;
import com.mo.model.Property;
import com.mo.model.User;

import jakarta.persistence.LockModeType;

public interface BookingDetailsRepository extends JpaRepository<BookingDetails, Long> {

	public BookingDetails findByBookingId(String bookingId);

	List<BookingDetails> findByUser(User user);

	List<BookingDetails> findByProperty(Property property);

	// to check if the overlapping booking exist but works in small concurrency apps
	// if there are more booking this will not ensure the integrity

//	@Query("SELECT COUNT(b) > 0 FROM BookingDetails b WHERE b.property.id = :propertyId AND " +
//		       "(b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate)")
//		boolean existsOverlappingBooking(
//		    @Param("propertyId") Long propertyId,
//		    @Param("checkInDate") LocalDate checkInDate,
//		    @Param("checkOutDate") LocalDate checkOutDate
//		);
	
	// better ffor high concurrency bookings as it locks the date ranges for till the booking is done.
	@Query("SELECT b FROM BookingDetails b WHERE b.property.id = :propertyId " +
		       "AND b.checkInDate < :checkOutDate " +
		       "AND b.checkOutDate > :checkInDate")
		@Lock(LockModeType.PESSIMISTIC_WRITE)
		List<BookingDetails> findOverlappingBookingsWithLock(
		    @Param("propertyId") Long propertyId,
		    @Param("checkInDate") LocalDate checkInDate,
		    @Param("checkOutDate") LocalDate checkOutDate
		);

}
