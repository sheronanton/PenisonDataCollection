package com.eodb.eodb_tariff.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pensioner_details")  // Adjust the table name as needed

public class PensionerDetailsEntity {

	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    private int ppoNo;

	    private String pensionerType;
	    
	    private String pensionerInitial;
	    
	    private String pensionerName;
	    
	    private String designation;

	    private Date dateOfJoining;

	    private Date dateOfBirth;

	    private Date dateOfRetirement;

	    private String gender;

	    private String panNumber;

	    private Long aadharNumber;

	    private Long contactCell;

	    private Long contactLandline;

	    @Column(name = "contact_email_id")
	    private String emailId;

	    @Column(name = "payment_office_id")
	    private Long officeId;

	    private String officeName;



		private Double  grossPay;

	    private Double  netPay;

	    private String permanentAddressLine1;

	    private String permanentAddressLine2;

	    private String permanentAddressLine3;

	    private String permanentDistrict;

	    private String permanentState;

	    private Long permanentPincode;

	    private String tempAddressLine1;

	    private String tempAddressLine2;

	    private String tempAddressLine3;

	    private String tempDistrict;

	    private String tempState;

	    private Long tempPincode;
	    
	    private Date dateOfDeath;
	    
	    private String relation;
	    private Integer employeeId;
		
		private String status;
		
		private String employeeName;

	    



		
		


}
