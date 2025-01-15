package com.mo.service.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mo.config.JwtUtils;
import com.mo.domain.AccountStatus;
import com.mo.domain.UserRole;
import com.mo.model.HostUser;
import com.mo.model.User;
import com.mo.model.VerificationCode;
import com.mo.repository.UserRepository;
import com.mo.repository.VerificationCodeRepository;
import com.mo.requestDTO.LoginRequest;
import com.mo.requestDTO.VerificationRequest;
import com.mo.response.AuthResponse;
import com.mo.service.AuthService;
import com.mo.service.IEmailService;
import com.mo.service.IHostUserService;
import com.mo.service.IUserService;
import com.mo.utils.HelperUtil;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IAuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	private final UserRepository userRepo;
	private final IUserService userService;
	private final IHostUserService hostService;
	private final IEmailService emailService;
	private final VerificationCodeRepository verificationCodeRepo;
	//private final ICustomUserAuthServiceImpl customeUserAuthServiceImpl;
	private final JwtUtils jwtUtils;

	@Override
	public AuthResponse userSignin(LoginRequest request) {
		return authenticateAndGenerateToken(request.getEmail(), request.getPassword(), UserRole.USER);
	}

	@Override
	public AuthResponse hostSignin(LoginRequest request) {
		return authenticateAndGenerateToken(request.getEmail(), request.getPassword(), UserRole.HOST);
	}

	private AuthResponse authenticateAndGenerateToken(String email, String password, UserRole role) {
		// Authenticate
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password provided", e);
		}

		// Load user details based on role
		UserDetails userDetails;
		if (role == UserRole.USER) {
			User user = userService.getUserByEmail(email);
			if (user == null) {
				throw new UsernameNotFoundException("User not found");
			}
			userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
			System.out.printf(userDetails.getUsername() + userDetails.getPassword());
		} else {
			HostUser hostUser = hostService.getHostUserByEmail(email);
			if (hostUser == null) {
				throw new UsernameNotFoundException("Host user not found");
			}
			userDetails = new org.springframework.security.core.userdetails.User(hostUser.getEmail(),
					hostUser.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_HOST")));
		}

		// Generate JWT
		String token = jwtUtils.generateJwtToken(userDetails, "ROLE_" + role.name());

		// Prepare response
		AuthResponse response = new AuthResponse();
		response.setJwt(token);
		response.setMessage("Login successful");
		response.setRole(role.name());
		return response;
	}

	@Override
	public boolean isUserEmailExists(String email, UserRole role) {
		if (role.equals(UserRole.USER)) {
			User user = userService.getUserByEmail(email);
			if (user != null) {
				return true;
			}
			return false;
		} else {
			HostUser hostUser = hostService.getHostUserByEmail(email);
			if (hostUser != null) {
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean isUserAddharNumberExist(String aadharNumber, UserRole role) {
		if (role.equals(UserRole.USER)) {
			User user = userRepo.findByAadharNumber(aadharNumber);
			if (user != null) {
				return true;
			}
			return false;
		} else {
			HostUser hostUser = hostService.getHostUserByAadharNumber(aadharNumber);
			if (hostUser!=null) {
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean sendVerificationOtp(String email, UserRole role) throws MessagingException {
		if (role.equals(UserRole.USER)) {
			User user = userRepo.findByEmail(email);
			if (user != null) {
				String otp = HelperUtil.generateOtp();
				String Subject = "Email Verification";
				String text = "Dear, " + user.getFirstName() + " " + user.getLastName()
						+ "\n Thank you for registering with 'Stakation'. \n"
						+ "Use this otp to varify your email address. \n \t ( " + otp + " )";

				VerificationCode verificationCode = new VerificationCode();
				verificationCode.setEmail(email);
				verificationCode.setOtpCode(otp);
				VerificationCode codeSaved = verificationCodeRepo.save(verificationCode);
				if (codeSaved != null) {
					emailService.sendVerificationOtpEmail(email, Subject, text);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (role.equals(UserRole.HOST)) {
			
			HostUser hostUser = hostService.getHostUserByEmail(email);
			if (hostUser!=null) {
				String otp = HelperUtil.generateOtp();
				String Subject = "Email Verification";
				String text = "Dear, " + hostUser.getFirstName() + " " + hostUser.getLastName()
						+ "\n Thank you for registering yourself as a host with 'Stakation'. \n"
						+"we hope that you will host many people at property"
						+ "\nUse this otp to varify your email address and complet the registration process. \n \t ( " + otp + " )";

				VerificationCode verificationCode = new VerificationCode();
				verificationCode.setEmail(email);
				verificationCode.setOtpCode(otp);
				VerificationCode codeSaved = verificationCodeRepo.save(verificationCode);
				if (codeSaved != null) {
					emailService.sendVerificationOtpEmail(email, Subject, text);
					return true;
				} else {
					return false;
				}
			}
			
			return false;
		} else {
			return false;
		}
	}

	@Override
	public boolean setAccountVerified(String email, String otp) {
		VerificationCode verificationCode = verificationCodeRepo.findByEmail(email);
		if (verificationCode == null || !verificationCode.getOtpCode().equals(otp)) {
			throw new BadCredentialsException("Wrong OTP");
		} else {
			User user = userService.getUserByEmail(email);
			user.setAccountStatus(AccountStatus.ACTIVE);
			userRepo.save(user);
			return true;
		}
	}

	@Override
	public boolean setHostAccountVerified(VerificationRequest request) {
		VerificationCode verificationCode = verificationCodeRepo.findByEmail(request.getEmail());
		if (verificationCode == null || !verificationCode.getOtpCode().equals(request.getOtp())) {
			throw new BadCredentialsException("Wrong Otp");
		}
		HostUser hostUser = hostService.getHostUserByEmail(request.getEmail());
		hostUser.setAccountStatus(AccountStatus.ACTIVE);
		HostUser saveHostUser = hostService.saveHostUser(hostUser);
		if (saveHostUser!=null) {
			return true;			
		}
		return false;
	}
	
	

}
