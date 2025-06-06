package com.twad.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PanchayatReport {
	private String officeId;
	private String officeName;
	private String districtCode;
	private String districtName;
	private String blockSno;
	private String blockName;
	private String panchayatCode;
	private String panchayatName;
	private String beneficiaryCode;
	private String lgdCode;
	private String billSno;
	private BigDecimal demandWc;
	private BigDecimal demandInt;
	private BigDecimal totalDmd;
}
