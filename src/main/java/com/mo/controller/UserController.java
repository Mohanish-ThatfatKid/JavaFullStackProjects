package com.mo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mo.model.Address;
import com.mo.model.BankDetails;
import com.mo.model.User;
import com.mo.response.ApiResponse;
import com.mo.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final IUserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserByIdHandler(@PathVariable Long id) throws Exception{
		User user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}
	
	
	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt) {
		String email = userService.getuserEmailFromJWt(jwt);

		User user = userService.getUserByEmail(email);
		return ResponseEntity.ok(user);
	}

	@PatchMapping("/update-Bankdetails")
	public ResponseEntity<User> updateBandDetailsHandler(@RequestBody BankDetails bankDetails,
			@RequestHeader("Authorization") String jwt) {

		String email = userService.getuserEmailFromJWt(jwt);
		User user = userService.UpdateUserBankDetails(bankDetails, email);
		return ResponseEntity.ok(user);
	}

	@PatchMapping("/update-address")
	public ResponseEntity<User> updateUserAddressHandler(@RequestBody Address address,
			@RequestHeader("Authorization") String jwt) {
		String email = userService.getuserEmailFromJWt(jwt);
		User user = userService.updateUserAddressDetails(address, email);

		return ResponseEntity.ok(user);
	}

	@PatchMapping("/update-profileimage/{profileImage}")
	public ResponseEntity<ApiResponse> updateUserProfilePicture(@RequestHeader("Authorization") String jwt,
			@PathVariable String profileImage) {
		String email = userService.getuserEmailFromJWt(jwt);
		
		User user = userService.updateUserProfilePicture(profileImage, email);
		if (user!=null) {
		return new ResponseEntity<>(new ApiResponse("profile image Updated"), HttpStatus.OK);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse("Profile image Updation failed"), HttpStatus.NOT_MODIFIED);
	}

}
