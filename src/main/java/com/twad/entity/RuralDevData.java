package com.twad.entity;

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

@Table(name="rd_transaction")
//@Table(name="rd_village_panchayat")
public class RuralDevData {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private int billSno;
	private Date receiptDate;
	private int receiptAmount;
	private int upiId;
	private String status;
	private String freezed;
	private int beneficiary_sno;
	private String district_name;
	private String block_name;
	private String village_name;
	private int village_lgdcode;
	private int offices_id;
	private String offices_name;
}
