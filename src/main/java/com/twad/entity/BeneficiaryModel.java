package com.twad.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pms_dcb_mst_beneficiary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeneficiaryModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long beneficiarySno;
	
	private String beneficiaryName;

}
