package com.mo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mo.domain.UserRole;
import com.mo.model.User;
import com.mo.response.ApiResponse;
import com.mo.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private AuthService authService;
	
	@PostMapping("/user-signup")
	public ResponseEntity<ApiResponse> createUserHandler(@RequestBody User user){
		boolean isExists = authService.isUserEmailExists(user.getEmail(), UserRole.USER);
		
		
		return null;
	}
}
