package com.mo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mo.config.JwtUtils;
import com.mo.domain.AccountStatus;
import com.mo.domain.UserRole;
import com.mo.model.User;
import com.mo.model.VerificationCode;
import com.mo.repository.UserRepository;
import com.mo.repository.VerificationCodeRepository;
import com.mo.requestDTO.LoginRequest;
import com.mo.response.AuthResponse;
import com.mo.service.AuthService;
import com.mo.service.IEmailService;
import com.mo.utils.HelperUtil;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IAuthServiceImpl implements AuthService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	private final UserRepository userRepo;
	private final IEmailService emailService;
	private final VerificationCodeRepository verificationCodeRepo;
	private final ICustomUserAuthServiceImpl customeUserAuthServiceImpl;
	private final JwtUtils jwtUtils;

	@Override
	public AuthResponse userSigin(LoginRequest request) {
		String email = request.getEmail();
		String password = request.getPassword();
		String role = UserRole.USER.toString();
		UserDetails userDetails = authentication(email,password,role);
		
		String token = jwtUtils.generateJwtToken(userDetails, role);
		AuthResponse response = new AuthResponse();
		response.setJwt(token);
		response.setMessage("Login Successful");
		response.setRole(role);
		
		return response;
	}

	private UserDetails authentication(String username,String password,String role) {
//		UserDetails userDetails = customeUserAuthServiceImpl.loadUserByUsername(username);
//		if (userDetails == null) {
//			throw new BadCredentialsException("Invalid Username or Password");
//		}
//		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
//				null, userDetails.getAuthorities());
//		if (!authenticationToken.isAuthenticated()) {
//			throw new BadCredentialsException("Invalid Username or Password");
//		} else {
//			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//			return userDetails;
//		}
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password provided", e);
		}
		username = role+username;
		UserDetails userDetails = customeUserAuthServiceImpl.loadUserByUsername(username);
		return userDetails;
		
		
	}

	@Override
	public AuthResponse hostSigin(LoginRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserEmailExists(String email, UserRole role) {
		if (role.equals(UserRole.USER)) {
			User user = userRepo.findByEmail(email);
			if (user != null) {
				return true;
			}
			return false;
		} else {
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
			return false;
		} else {
			return false;
		}
	}

	@Override
	public void setAccountVerified(String email, String otp) {
		VerificationCode verificationCode = verificationCodeRepo.findByEmail(email);
		if (verificationCode != null && verificationCode.getOtpCode().equals(otp)) {
			User user = userRepo.findByEmail(email);
			user.setAccountStatus(AccountStatus.ACTIVE);
			userRepo.save(user);
		} else {
//			throw new BadCredentialsException("Wrong OTP"); uncomment after adding spring boot security starters
		}
	}
}
