package com.mo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mo.domain.UserRole;
import com.mo.model.User;
import com.mo.model.VerificationCode;
import com.mo.requestDTO.LoginRequest;
import com.mo.requestDTO.VerificationRequest;
import com.mo.response.ApiResponse;
import com.mo.response.AuthResponse;
import com.mo.service.AuthService;
import com.mo.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final IUserService userService;
	
	@PostMapping("/register/user")
	public ResponseEntity<ApiResponse> createUserHandler(@RequestBody User user) throws Exception{
		System.out.println("AuthController.createUserHandler()");
		boolean isExists = authService.isUserEmailExists(user.getEmail(), UserRole.USER);
		if (!isExists) {
			boolean registerUserDetails = userService.registerUserDetails(user);
			if (registerUserDetails) {
				boolean sendVerificationOtp = authService.sendVerificationOtp(user.getEmail(), UserRole.USER);
				if (sendVerificationOtp) {
					ApiResponse response = new ApiResponse();
					response.setMessage("User Registration Successful! we ahve send a varification code to your email address please varify your email address");
					return ResponseEntity.ok(response);
				}
			}
			return new ResponseEntity<ApiResponse>(new ApiResponse("User Registration failed"), HttpStatus.BAD_REQUEST);
		}
		
		
		return new ResponseEntity<ApiResponse>(new ApiResponse("User Already Exists try Login instead"), HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/verify/user")
	public ResponseEntity<ApiResponse> verifyOtp(@RequestBody VerificationRequest request){
		System.out.println("AuthController.verifyOtp()");
		System.out.println(request.getEmail());
		authService.setAccountVerified(request.getEmail(),request.getOtp());
		System.out.println("Returned from method");
		return ResponseEntity.ok(new ApiResponse("Account verified Successfully"));
		
	}
	
	@PostMapping("/signin/user")
	public ResponseEntity<AuthResponse> userSigninHandler(@RequestBody LoginRequest request){
		AuthResponse response = new AuthResponse();
		response = authService.userSignin(request);
		if (response==null) {
			response.setMessage("Login Failed");
			
			return new ResponseEntity<AuthResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok(response);
		
	}
	
}
