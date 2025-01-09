package com.mo.service;

import java.util.List;

import com.mo.model.Review;
import com.mo.model.User;
import com.mo.requestDTO.ReviewRequest;

public interface IReviewService {

	public Review createReview(ReviewRequest request, Long id, User user);
	
	public Review deleteReview(Long id, User user);
	
	public List<Review> getAllReviewsByUser(String email);
	
	public List<Review> getAllReviewsByProperty(Long propertyId);
	
}
