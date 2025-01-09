package com.mo.service;

import com.mo.model.Address;
import com.mo.model.BankDetails;
import com.mo.model.HostUser;

public interface IHostUserService {

	public HostUser getHostUserById(Long id) throws Exception;
	
	public HostUser getHostUserByEmail(String email);
	
	public HostUser getHostUserByAadharNumber(String aadharNumber);
	
	public void deleteHostUserById(Long id);
	
	public void deleteHostUser(HostUser user);
	
	public HostUser updateHostUserEmail(String email, HostUser hostUser);
	
	public HostUser updateHostUserAddress(Address address, String email) throws Exception;
	
	public HostUser UpdateHostUserMobileNumber(String mobileNumber, String email) throws Exception;
	
	public HostUser updateHostUserBankDetails(BankDetails bankDetails, String email) throws Exception;
	
	public HostUser updateHostUserProfileImage(String profileImage, String email) throws Exception;
	
	
	
	
}
