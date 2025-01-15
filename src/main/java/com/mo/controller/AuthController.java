package com.mo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mo.domain.UserRole;
import com.mo.model.HostUser;
import com.mo.model.User;
import com.mo.requestDTO.LoginRequest;
import com.mo.requestDTO.VerificationRequest;
import com.mo.response.ApiResponse;
import com.mo.response.AuthResponse;
import com.mo.service.AuthService;
import com.mo.service.IHostUserService;
import com.mo.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final IUserService userService;
	private final IHostUserService hostService;

	@PostMapping("/register/user")
	public ResponseEntity<ApiResponse> createUserHandler(@RequestBody User user) throws Exception {
		System.out.println("AuthController.createUserHandler()");
		boolean isExists = authService.isUserEmailExists(user.getEmail(), UserRole.USER);
		if (!isExists) {
			boolean registerUserDetails = userService.registerUserDetails(user);
			if (registerUserDetails) {
				boolean sendVerificationOtp = authService.sendVerificationOtp(user.getEmail(), UserRole.USER);
				if (sendVerificationOtp) {
					ApiResponse response = new ApiResponse();
					response.setMessage(
							"User Registration Successful! we ahve send a varification code to your email address please varify your email address");
					return ResponseEntity.ok(response);
				}
			}
			return new ResponseEntity<ApiResponse>(new ApiResponse("User Registration failed"), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<ApiResponse>(new ApiResponse("User Already Exists try Login instead"),
				HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/verify/user")
	public ResponseEntity<ApiResponse> verifyOtp(@RequestBody VerificationRequest request) {
		System.out.println(request.getEmail());
		authService.setAccountVerified(request.getEmail(), request.getOtp());
		return ResponseEntity.ok(new ApiResponse("Account verified Successfully"));

	}

	@PostMapping("/signin/user")
	public ResponseEntity<AuthResponse> userSigninHandler(@RequestBody LoginRequest request) {
		AuthResponse response;
		response = authService.userSignin(request);
		if (response == null) {
			response = new AuthResponse();
			response.setMessage("Login Failed");

			return new ResponseEntity<AuthResponse>(response, HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok(response);

	}

	// host auth mappings

	@PostMapping("/register/host")
	public ResponseEntity<ApiResponse> createHostUserHandler(@RequestBody HostUser hostUser) throws Exception {
		System.out.println("AuthController.createHostUserHandler()");
		boolean isExists = authService.isUserEmailExists(hostUser.getEmail(), UserRole.HOST);
		if (!isExists) {
			boolean registerUserDetails = hostService.registerHostUser(hostUser);
			if (registerUserDetails) {
				boolean sendVerificationOtp = authService.sendVerificationOtp(hostUser.getEmail(), UserRole.HOST);
				if (sendVerificationOtp) {
					ApiResponse response = new ApiResponse();
					response.setMessage(
							"Your Registration as a host is Successful! we have send a varification code to your email address please varify your email address");
					return ResponseEntity.ok(response);
				}
			}
			return new ResponseEntity<ApiResponse>(new ApiResponse("Host Registration failed"), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<ApiResponse>(new ApiResponse("Host Already Exists try Login instead"),
				HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/signin/host")
	public ResponseEntity<AuthResponse> hostSignInHandler(@RequestBody LoginRequest request) {
		AuthResponse response;
		response = authService.hostSignin(request);
		if (response == null) {
			response = new AuthResponse();
			response.setMessage("Login Failed");

			return new ResponseEntity<AuthResponse>(response, HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/verify/host")
	public ResponseEntity<ApiResponse> hostAccountVerifyHandler(@RequestBody VerificationRequest request) {
		boolean flag = authService.setHostAccountVerified(request);
		if (flag) {
			return ResponseEntity.ok(new ApiResponse("Account verification successfull"));
		}

		return new ResponseEntity<ApiResponse>(new ApiResponse(
				"Account verification failed please try again and enter the OTP sent to your registered email address"),
				HttpStatus.BAD_REQUEST);
	}

}
