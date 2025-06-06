package com.eodb.eodb_tariff.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="fas_rd_reverted_data")
public class VerifyRuralDevData {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private int billSno;
	private Date receiptDate;
	private int receiptAmount;
	private int utr_no;
	private int beneficiary_sno;
	private String district_name;
	private String block_name;
	private String village_name;
	private String village_lgd_code;
	private int office_id;
	private String office_name;
	private String verify;
	private String reason;
}
