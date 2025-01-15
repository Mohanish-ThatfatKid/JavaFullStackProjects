package com.mo.service;

import com.mo.model.Address;
import com.mo.model.BankDetails;
import com.mo.model.HostUser;

public interface IHostUserService {

	public boolean registerHostUser(HostUser hostUser);
	
	public HostUser getHostUserById(Long id) throws Exception;

	public HostUser getHostUserByEmail(String email);

	public HostUser getHostUserByAadharNumber(String aadharNumber);

	public void deleteHostUserById(Long id);

	public void deleteHostUser(HostUser user);

	public HostUser updateHostUserEmail(String email, HostUser hostUser);

	public HostUser updateHostUserAddress(Address address, String email);

	public HostUser UpdateHostUserMobileNumber(String mobileNumber, String email) throws Exception;

	public HostUser updateHostUserBankDetails(BankDetails bankDetails, String email);

	public HostUser updateHostUserProfileImage(String profileImage, String email);

	public String getHostUserEmailFromJwt(String jwt);

	public HostUser saveHostUser(HostUser hostUser);

}
