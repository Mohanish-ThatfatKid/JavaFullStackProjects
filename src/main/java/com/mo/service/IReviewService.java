package com.mo.service;

import java.util.List;

import com.mo.model.Review;
import com.mo.requestDTO.ReviewRequest;

public interface IReviewService {

	public Review createReview(ReviewRequest request, Long id, String email);
	
	public void deleteReview(Long id, String email);
	
	public List<Review> getAllReviewsByUser(String email);
	
	public List<Review> getAllReviewsByProperty(Long propertyId);
	
}
