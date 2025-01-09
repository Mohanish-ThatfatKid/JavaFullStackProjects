package com.mo.requestDTO;

import lombok.Data;

@Data
public class SignupRequest {

	private String firstName;
	private String lastName;
	
	private String email;
	private String password;
	
	private String profileImage;
	
	private String mobileNumber;
	
	private String aadharNumber;
	
	
	
}
