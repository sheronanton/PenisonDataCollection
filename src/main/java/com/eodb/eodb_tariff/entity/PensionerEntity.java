package com.eodb.eodb_tariff.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name ="twad_pensioner_details")
@Data
public class PensionerEntity {
	@Id 
	private int id;
	
	private int ppoNo;
	
	private String bankCode;
	
	private String lifeCertificate;
	
	private String reMarriage;
	
	private String reEmployed;
	
	private LocalDateTime authenticationDate;
	
	private String pramaanId;
	
	private String aadhaarNo;
	
	private String mobileNumber;
	
	private String name;
	
	
	private String gender;
	
	private Date dateOfBirth;
	
	private String bankAccount;
	
	private String bankName;
	
	private String branchName;
	
	private String pensionerTypeId;
	
	
	private String pensionerType;


	private int paymentOfficeId;
	
	
	private String paymentOfficeName;
	
	private String panNo;
	
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;

	private String state;
	private String district;
	private int pincode;
	private String email;
	private String aadhaarStatus;
	


}

