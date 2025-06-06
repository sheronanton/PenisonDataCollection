package com.eodb.eodb_tariff.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eodb.eodb_tariff.entity.PensionerDetailsEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBean {

	List<Map<String, Object>> userName;

//	List<Map<String,Object>> userData;

	int[] userData;
	String [] impData;

	List<Map<String, Object>> result;

	String key[];
	List<Map<String, Object>> data;
	String xAxis;
	String yAxis;
	Object test;
	

	int isError = 0;
	String message1 = "Success";
	UserBean user;

	List<Map<String, Object>> districts;
	List<Map<String, Object>> blocks;
	List<Map<String, Object>> panchayats;
	List<Map<String, Object>> beneficiaries;
	List<Map<String, Object>> response;

	List<OfficeDistrictBean> officeDistricts;
//	List<BeneficiaryBean> beneficiaries;
	
	List<OfficeBean> offices;


	List<Map<String,Object>> checkedData;

	OfficeBean officeBean;


	

	Long circleId;
	String circleName;
	Long officeId;
	String officeName;


	private Date updatedOn;

	List<Map<String, Object>> metrestatus;

	List<EbOfficeBean> ebOffices;
	
	List<Map<String,Object>> demandReport;

	Long schemeId;
	String schemeName;
	String typeOfMetre;
	Long consumerCount;
	Long consumerCharges;
	int count;


	///////////////////////////////////
	
	
	private String message="failure";

	private PensionerDetailsEntity pensionerDetailsEntity;
	
	public PensionerDetailsEntity getPensionerDetailsEntity() {
		return pensionerDetailsEntity;
	}

	public void setPensionerDetailsEntity(PensionerDetailsEntity pensionerDetailsEntity) {
		this.pensionerDetailsEntity = pensionerDetailsEntity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	//////////////////////////////////////
	
	public List<EbOfficeBean> getEbOffices() {
		return this.ebOffices;
	}

	public void setEbOffices(List<EbOfficeBean> ebOffices) {
		this.ebOffices = ebOffices;
	}

	public List<Map<String, Object>> getdemandReport() {
		return this.demandReport;
	}
	
	public void setdemandReport(List<Map<String, Object>> demandReport) {
		this.demandReport = demandReport;
	}

	public int getIsError() {
		return isError;
	}

	public void setIsError(int isError) {
		this.isError = isError;
	}

	public String getMessage1() {
		return message;
	}

	public void setMessage1(String message) {
		this.message = message;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public List<OfficeDistrictBean> getOfficeDistricts() {
		return officeDistricts;
	}

	public void setOfficeDistricts(List<OfficeDistrictBean> officeDistricts) {
		this.officeDistricts = officeDistricts;
	}


	


	public OfficeBean getOfficeBean() {
		return officeBean;
	}

	public void setOfficeBean(OfficeBean officeBean) {
		this.officeBean = officeBean;
	}



	public List<OfficeBean> getOffices() {
		return offices;
	}

	public void setOffices(List<OfficeBean> offices) {
		this.offices = offices;
	}
	
	public void setImpData(String[] impData) {
		this.impData=impData;
	}
	
	public String[] getImpData() {
		return impData;
	}
	

	public List<Map<String, Object>> getCheckedData() {
		return this.checkedData;
	}

	public void setCheckedData(List<Map<String, Object>> checkedData) {
		this.checkedData = checkedData;
	}
	
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count=count;
	}
}
