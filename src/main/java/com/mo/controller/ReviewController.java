package com.mo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mo.model.Review;
import com.mo.requestDTO.ReviewRequest;
import com.mo.response.ApiResponse;
import com.mo.service.IReviewService;
import com.mo.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

	private final String authorizationHeader = "Authorization";
	private final IReviewService reviewService;
	private final IUserService userService;

	// here we are passing the property id
	@PostMapping("/create/{id}")
	public ResponseEntity<ApiResponse> createReviewHandler(@RequestHeader(authorizationHeader) String jwt,
			@RequestBody ReviewRequest request, @PathVariable Long id) {

		String email = userService.getuserEmailFromJWt(jwt);
		Review review = reviewService.createReview(request, id, email);
		if (review != null) {
			return new ResponseEntity<>(new ApiResponse("Review Created successfully"), HttpStatus.OK);
		}

		return new ResponseEntity<>(new ApiResponse("Failed to create review try again after some time"),
				HttpStatus.BAD_REQUEST);
	}
}
