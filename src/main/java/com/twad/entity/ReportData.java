package com.twad.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportData {
	private String officeName;
	private long officeId;
	private String districtCode;
	private String districtName;
	private long beneficiarySno;
	private String beneficiaryName;
	private int obWc;
	private int colnWc;
	private int balanceWc;
	private int dmdWc;
	private int totalWc;
	private int obInt;
	private int colnInt;
	private int balanceInt;
	private int dmdInt;
	private int totalInt;
	private int total;

	// Getters and Setters
}
