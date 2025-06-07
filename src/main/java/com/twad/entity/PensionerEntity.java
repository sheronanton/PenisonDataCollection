package com.twad.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name ="twad_pensioner_details")
@EntityListeners(AuditingEntityListener.class)
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
	
	private LocalDate  dateOfBirth;
	
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
	private Integer  pincode;
	private String email;
	private String aadhaarStatus;
	
	private String flag;
	
	private String updatedBy;
	
	@LastModifiedDate
	private LocalDateTime updatedDate;

	


}

