package com.mo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mo.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

	@GetMapping("/")
	public ResponseEntity<ApiResponse> homeControllerHandler(){
		ApiResponse response = new ApiResponse();
		response.setMessage("Welcome to stakation your travel friendly homes");
		return ResponseEntity.ok(response);
	}
	
}
