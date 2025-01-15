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
import com.mo.model.HostUser;
import com.mo.response.ApiResponse;
import com.mo.service.IHostUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/host")
public class HostUserController {

	private final IHostUserService hostService;

	@GetMapping("/profile")
	public ResponseEntity<HostUser> getHostUserProfileHandler(@RequestHeader("Authorization") String jwt) {
		String email = hostService.getHostUserEmailFromJwt(jwt);
		HostUser hostUser = hostService.getHostUserByEmail(email);
		if (hostUser != null) {
			return ResponseEntity.ok(hostUser);
		}
		return null;
	}

	@PatchMapping("/update-Bankdetails")
	public ResponseEntity<HostUser> updateBandDetailsHandler(@RequestBody BankDetails bankDetails,
			@RequestHeader("Authorization") String jwt) {

		String email = hostService.getHostUserEmailFromJwt(jwt);
		HostUser hostUser = hostService.updateHostUserBankDetails(bankDetails, email);
		return ResponseEntity.ok(hostUser);
	}
	
	@PatchMapping("/update-address")
	public ResponseEntity<HostUser> updateHostAddressHandler(@RequestBody Address address,
			@RequestHeader("Authorization") String jwt) {
		String email = hostService.getHostUserEmailFromJwt(jwt);
		HostUser hostUser = hostService.updateHostUserAddress(address, email);

		return ResponseEntity.ok(hostUser);
	}
	
	@PatchMapping("/update-profileimage/{profileImage}")
	public ResponseEntity<ApiResponse> updateUserProfilePicture(@RequestHeader("Authorization") String jwt,
			@PathVariable String profileImage) {
		String email = hostService.getHostUserEmailFromJwt(jwt);
		
		HostUser hostUser = hostService.updateHostUserProfileImage(profileImage, email);
		if (hostUser!=null) {
		return new ResponseEntity<>(new ApiResponse("profile image Updated"), HttpStatus.OK);
		}
		return new ResponseEntity<ApiResponse>(new ApiResponse("Profile image Updation failed"), HttpStatus.NOT_MODIFIED);
	}

}
