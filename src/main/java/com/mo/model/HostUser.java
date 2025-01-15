package com.mo.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mo.domain.AccountStatus;
import com.mo.domain.UserRole;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HostUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String firstName;

	private String lastName;

	private String profileImage;

	@Email
	private String email;

	private String password;

	private String mobileNumber;

	private String aadharNumber;

	@OneToOne
	private Address address;

	private UserRole role = UserRole.HOST;

	@Embedded
	private BankDetails bankDetails;
	
	@CreationTimestamp
	private Instant createdOn;
	
	@UpdateTimestamp
	private Instant lastModifiedOn;
	
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus = AccountStatus.INACTIVE;
}
