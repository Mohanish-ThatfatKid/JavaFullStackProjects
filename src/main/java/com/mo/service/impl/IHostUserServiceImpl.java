package com.mo.service.impl;

import org.springframework.stereotype.Service;

import com.mo.model.Address;
import com.mo.model.BankDetails;
import com.mo.model.HostUser;
import com.mo.repository.AddressRepository;
import com.mo.repository.HostUserRepository;
import com.mo.service.IHostUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IHostUserServiceImpl implements IHostUserService {

	private final HostUserRepository hostRepo;
	private final AddressRepository addressRepo;

	@Override
	public HostUser getHostUserById(Long id) throws Exception {
		HostUser hostUser = hostRepo.findById(id).get();

		if (hostUser == null) {
			throw new Exception("Host not found with provided Id");
		}

		return hostUser;
	}

	@Override
	public HostUser getHostUserByEmail(String email) {
		return hostRepo.findByEmail(email);
	}

	@Override
	public HostUser getHostUserByAadharNumber(String aadharNumber) {
		HostUser hostUser = hostRepo.findByAadharNumber(aadharNumber);
		if (hostUser == null) {
			return null;
		}
		return hostUser;
	}

	@Override
	public void deleteHostUserById(Long id) {
		hostRepo.deleteById(id);
	}

	@Override
	public void deleteHostUser(HostUser user) {
		hostRepo.delete(user);
	}

	@Override
	public HostUser updateHostUserEmail(String email, HostUser hostUser) {

		return null;
	}

	@Override
	public HostUser updateHostUserAddress(Address address, String email) throws Exception {

		HostUser hostUser = this.getHostUserByEmail(email);
		Address savedAddress = addressRepo.findById(hostUser.getAddress().getId()).get();
		addressRepo.delete(savedAddress);
		hostUser.setAddress(address);

		return hostRepo.save(hostUser);

	}

	@Override
	public HostUser UpdateHostUserMobileNumber(String mobileNumber, String email) throws Exception {

		HostUser hostUser = this.getHostUserByEmail(email);

		hostUser.setMobileNumber(mobileNumber);
		return hostRepo.save(hostUser);

	}

	@Override
	public HostUser updateHostUserBankDetails(BankDetails bankDetails, String email) throws Exception {

		HostUser hostUser = this.getHostUserByEmail(email);

		hostUser.setBankDetails(bankDetails);
		return hostRepo.save(hostUser);

	}

	@Override
	public HostUser updateHostUserProfileImage(String profileImage, String email) throws Exception {

		HostUser hostUser = this.getHostUserByEmail(email);

		hostUser.setProfileImage(profileImage);

		return hostRepo.save(hostUser);

	}

}
