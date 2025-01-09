package com.mo.service;

import com.mo.model.Address;
import com.mo.model.BankDetails;
import com.mo.model.User;

public interface IUserService {

	public boolean registerUserDetails(User user) throws Exception;

	public User getUserById(Long id) throws Exception;

	public User getUserByEmail(String email);

	public User updateUserAddressDetails(Address address, String email);

	public User updateUserProfilePicture(String profilePicture, String email);

	public User UpdateUserBankDetails(BankDetails bankDetails, String email);

	public void deleteUserAccount(User user);

	void deleteUserAccountById(Long id);

}
