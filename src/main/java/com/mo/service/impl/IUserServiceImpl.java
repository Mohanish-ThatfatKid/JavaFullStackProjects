package com.mo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mo.config.JwtUtils;
import com.mo.domain.AccountStatus;
import com.mo.domain.UserRole;
import com.mo.exceptions.UserRegistrationException;
import com.mo.model.Address;
import com.mo.model.BankDetails;
import com.mo.model.User;
import com.mo.repository.AddressRepository;
import com.mo.repository.UserRepository;
import com.mo.service.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IUserServiceImpl implements IUserService {

	private final UserRepository userRepo;
	private final AddressRepository addressRepo;
	private final JwtUtils jwtUtils;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	public boolean registerUserDetails(User user) throws Exception {
		// Set default role and account status
		user.setRole(UserRole.USER);
		user.setAccountStatus(AccountStatus.INACTIVE);

		// Encrypt the user's password
		user.setPassword(encoder.encode(user.getPassword()));

		// Save the address first to ensure it's persisted before linking it to the user
		Address address = addressRepo.save(user.getAddress());
		if (address == null) {
			throw new UserRegistrationException("Address saving failed during registration");
		}

		// Save the user with the address
		user.setAddress(address);
		User savedUser = userRepo.save(user);

		// Validate that the user was saved successfully
		if (savedUser == null) {
			throw new UserRegistrationException("User registration failed !!!");
		}

		// No need to create authorities manually at this point unless it's used
		// elsewhere
		return true;

	}

	@Override
	public void deleteUserAccountById(Long id) {
		userRepo.deleteById(id);
	}

	@Override
	public void deleteUserAccount(User user) {

		User savedUser = userRepo.findByEmail(user.getEmail());
		userRepo.delete(savedUser);

	}

	@Override
	public User updateUserAddressDetails(Address address, String email) {
		User user = userRepo.findByEmail(email);
		if (user != null) {
			Address savedAddress = addressRepo.findById(user.getAddress().getId()).get();
			addressRepo.delete(savedAddress);
			Address newAddress = addressRepo.save(address);
			user.setAddress(newAddress);
			return userRepo.save(user);
		}
		return null;
	}

	@Override
	public User updateUserProfilePicture(String profilePicture, String email) {
		User user = getUserByEmail(email);
		System.out.println(user);
		if (user==null) {
			return null;
		}
		user.setProfileImage(profilePicture);
		return userRepo.save(user);
		
	}

	@Override
	public User UpdateUserBankDetails(BankDetails bankDetails, String email) {

		User user = userRepo.findByEmail(email);
		if (user != null) {
			user.setBankDetails(bankDetails);
			return userRepo.save(user);
		}

		return null;
	}

	@Override
	public User getUserById(Long id) throws Exception {
		User user = userRepo.findById(id).get();
		if (user == null) {
			throw new Exception("User Not found with given Id");
		}
		return user;
	}

	@Override
	public String getuserEmailFromJWt(String jwt) {

		jwt = jwt.substring(7);
		return jwtUtils.extractUsername(jwt);
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

}
