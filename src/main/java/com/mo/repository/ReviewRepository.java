package com.mo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mo.model.Property;
import com.mo.model.Review;
import com.mo.model.User;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long>{

	List<Review> findByUser(User user);
	List<Review> findByProperty(Property property);
	List<Review> findByRating(double rating);
}
