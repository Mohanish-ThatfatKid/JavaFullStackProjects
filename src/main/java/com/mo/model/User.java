package com.mo.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mo.domain.AccountStatus;
import com.mo.domain.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

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
	
	@Column(unique = true)
	private String aadharNumber;
	
	@OneToOne
	private Address address;
	
	private UserRole role = UserRole.USER;
	
	@Embedded
	private BankDetails bankDetails;
	
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<BookingDetails> booking = new ArrayList<>();
	
	@CreationTimestamp
	private Instant createdOn;
	
	@UpdateTimestamp
	private Instant lastModifiedOn;
	
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus = AccountStatus.INACTIVE;
	
	
}
