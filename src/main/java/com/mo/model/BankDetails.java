package com.mo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BankDetails {

	private String accountNumber;
	private String accountHolderName;
	private String IFSCCode;
}
