package com.eodb.eodb_tariff.bean;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.eodb.eodb_tariff.entity.PensionerDetailsEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RequestBean {

	int[] beneficiaries;
	Date date;
	String hashkey;

	Date rdReceiptDate;
	String upiId;
	String receiptDate;
	String year;
	String month;
	String userName;
	String password;
	
	int paymentOfficeId;

	private Long officeId ;
	private String typeOfConnection;
	private Long consumerNumber;
	private int demandYear;
	private int demandMonth;
	private String flag;
	private Long schemeId;
	private String sortColumn;
	private String officeList;
	private String chkString;
	private Map<String,String> verifyData;
	private String status;
	private Date fromDate;
	private Date toDate;
	
	
	//////////////
	
private String pensionerType;
	
	private Long aadhaarNumber;
	
	private PensionerDetailsEntity  pensionerDetailsEntity;
	
	private Long mobileNumber;
	
	

	public PensionerDetailsEntity getPensionerDetailsEntity() {
		return pensionerDetailsEntity;
	}

	public void setPensionerDetailsEntity(PensionerDetailsEntity pensionerDetailsEntity) {
		this.pensionerDetailsEntity = pensionerDetailsEntity;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Long getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(Long aadharNumber) {
		this.aadhaarNumber = aadharNumber;
	}

	public String getPensionerType() {
		return pensionerType;
	}

	public void setPensionerType(String pensionerType) {
		this.pensionerType = pensionerType;
	}

	
	////////////////////
	

	
	public Long getOfficeId() {
		return officeId;
	}
	
	public int getPaymentOfficeId() {
		return paymentOfficeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getTypeOfConnection() {
		return typeOfConnection;
	}

	public void setTypeOfConnection(String typeOfConnection) {
		this.typeOfConnection = typeOfConnection;
	}

	
	public String getChkString() {
		return chkString;
	}

	public void setChkString(String chkString) {
		this.chkString = chkString;
	}
	
	public Long getConsumerNumber() {
		return consumerNumber;
	}

	public void setConsumerNumber(Long consumerNumber) {
		this.consumerNumber = consumerNumber;
	}

	public int getDemandMonth() {
		return demandMonth;
	}

	public void setDemandMonth(int demandMonth) {
		this.demandMonth = demandMonth;
	}

	public int getDemandYear() {
		return demandYear;
	}

	public void setDemandYear(int demandYear) {
		this.demandYear = demandYear;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Long getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(Long schemeId) {
		this.schemeId = schemeId;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getOfficeList() {
		return officeList;
	}

	public void setOfficeList(String officeList) {
		this.officeList = officeList;
	}
	
	public void setUpiId(String upiId) {
		this.upiId=upiId;
	}
	
	public String getUpiId() {
		return upiId;
	}
	public Map<String,String> getVerifyData() {
		return verifyData;
	}

	public void setOffices(Map<String,String> verifyData) {
		this.verifyData = verifyData;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public void setFromDate(Date fromDate) {
		this.fromDate=fromDate;
	}
	
	public Date getFromDate() {
		return fromDate;
	}
	
	public void setToDate(Date toDate) {
		this.toDate=toDate;
	}
	
	public Date getToDate() {
		return toDate;
	}
	
	//////////////////
	
	
	

}
