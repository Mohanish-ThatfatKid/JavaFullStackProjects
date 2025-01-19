package com.mo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mo.exceptions.PropertyException;
import com.mo.exceptions.ReviewException;
import com.mo.exceptions.UserException;
import com.mo.model.Property;
import com.mo.model.Review;
import com.mo.model.User;
import com.mo.repository.ReviewRepository;
import com.mo.requestDTO.ReviewRequest;
import com.mo.service.IPropertyService;
import com.mo.service.IReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IReviewServiceImpl implements IReviewService {

	private final ReviewRepository reviewRepo;
	private final IPropertyService propService;
	private final IUserServiceImpl userService;

	@Override
	public Review createReview(ReviewRequest request, Long id, String email) {

		Property property = propService.getPropertyById(id);
		if (property == null) {
			throw new PropertyException("No Property found by provided ID.");
		}
		User user = userService.getUserByEmail(email);
		if (user == null) {
			throw new UserException("User not found from provided email address");
		}

		Review review = new Review();
		review.setProperty(property);
		review.setUser(user);
		review.setRating(request.getReviewRating());
		review.setReviewText(request.getReviewText());

		Review savedReview = reviewRepo.save(review);
		property.getReviews().add(savedReview);


		return savedReview;
	}

	@Override
	public void deleteReview(Long id, String email) {
		Review review = reviewRepo.findById(id).get();
		User user = userService.getUserByEmail(email);

		if (user == null) {
			throw new UserException("User not found from provided email address");
		}

		if (!review.getUser().getEmail().equalsIgnoreCase(email)) {
			throw new ReviewException("User is not authorize to delete this review");
		}

		reviewRepo.delete(review);

	}

	@Override
	public List<Review> getAllReviewsByUser(String email) {
		List<Review> userReviews = reviewRepo.findByUser(userService.getUserByEmail(email));
		if (userReviews.isEmpty() || userReviews==null) {
			return null;
		}
		return userReviews;
	}

	@Override
	public List<Review> getAllReviewsByProperty(Long propertyId) {
		List<Review> propertyReviews = reviewRepo.findByProperty(propService.getPropertyById(propertyId));
		if (propertyReviews.isEmpty() || propertyReviews==null) {
			return null;
		}
		return propertyReviews;
	}

}
