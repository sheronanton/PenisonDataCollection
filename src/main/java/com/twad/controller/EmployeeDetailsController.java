package com.twad.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twad.bean.*;
import java.util.Date;

import com.twad.entity.PensionerDetailsEntity;
import com.twad.entity.PensionerEntity;
import com.twad.repo.PensionerDetailsRepo;
import com.twad.service.PensionerDetailsService;

@RestController
@RequestMapping("employees")
@CrossOrigin
public class EmployeeDetailsController {

	
	@Autowired
	private PensionerDetailsService pensionerDetailsService;
	
	@Autowired
	private PensionerDetailsRepo detailsRepo;
	
	public Long safeParseLong(Object value) {
	    if (value instanceof String) {
	        String strValue = (String) value;
	        if (strValue.equals("null") || strValue.isEmpty()) {
	            return null; // Return null for "null" or empty strings
	        }
	        try {
	            return Long.valueOf(strValue);
	        } catch (NumberFormatException e) {
	            // Handle the exception if parsing fails
	            System.err.println("Failed to parse Long: " + strValue);
	            return null; // or some default value
	        }
	    } else if (value instanceof Long) {
	        return (Long) value;
	    }
	    return null; // Handle unexpected types
	}

	@PostMapping("/getPensionerDetails")
	public List<PensionerEntity> getEmployeeDetails(@RequestBody RequestBean bean) {
		
		String pensionerTypeId  = (String) bean.getPensionerTypeId();
		
		
		
		List<PensionerEntity> pensionDetails =pensionerDetailsService.getEmployeeDetails(bean.getPaymentOfficeId(), pensionerTypeId);



		return pensionDetails;
	}
	

	
//	@PostMapping("/savePensionerDetails")
//	public ResponseEntity<ResponseBean> savePensionerDetails(@RequestBody RequestBean requestBean){
//		
//		PensionerDetailsEntity pensionerDetailsEntity = requestBean.getPensionerEntity();
//		int ppoNo = pensionerDetailsEntity.getPpoNo();
//		Long mobileNumber = requestBean.getMobileNumber();
//		Long aadhaarNumber = requestBean.getAadhaarNumber();
//		
//	    PensionerDetailsEntity existingPensioner = pensionerDetailsService.findByPpoNo(ppoNo);
//
//	    if (existingPensioner != null) {
//	        // Update existing entity
//	    	  existingPensioner.setPensionerType(pensionerDetailsEntity.getPensionerType());
//	          existingPensioner.setPensionerInitial(pensionerDetailsEntity.getPensionerInitial());
//	          existingPensioner.setPensionerName(pensionerDetailsEntity.getPensionerName());
//	          existingPensioner.setDesignation(pensionerDetailsEntity.getDesignation());
//	          existingPensioner.setDateOfJoining(pensionerDetailsEntity.getDateOfJoining());
//	          existingPensioner.setDateOfBirth(pensionerDetailsEntity.getDateOfBirth());
//	          existingPensioner.setDateOfRetirement(pensionerDetailsEntity.getDateOfRetirement());
//	          existingPensioner.setGender(pensionerDetailsEntity.getGender());
//	          existingPensioner.setPanNumber(pensionerDetailsEntity.getPanNumber());
////	          if(mobileNumber!= null) {
////		          existingPensioner.setContactCell(mobileNumber);
////	          }else {
//	        	  existingPensioner.setContactCell(pensionerDetailsEntity.getContactCell());
////	          }
//
//	          
////	          if(aadhaarNumber!= 0) {
////		          existingPensioner.setAadharNumber(aadhaarNumber);
////	          }else {
//		          existingPensioner.setAadharNumber(pensionerDetailsEntity.getAadharNumber());
////	          }
//
//	          
//	          existingPensioner.setContactLandline(pensionerDetailsEntity.getContactLandline());
//	          existingPensioner.setEmailId(pensionerDetailsEntity.getEmailId());
//	          existingPensioner.setOfficeId(pensionerDetailsEntity.getOfficeId());
//	          existingPensioner.setOfficeName(pensionerDetailsEntity.getOfficeName());
//	          existingPensioner.setGrossPay(pensionerDetailsEntity.getGrossPay());
//	          existingPensioner.setNetPay(pensionerDetailsEntity.getNetPay());
//	          existingPensioner.setPermanentAddressLine1(pensionerDetailsEntity.getPermanentAddressLine1());
//	          existingPensioner.setPermanentAddressLine2(pensionerDetailsEntity.getPermanentAddressLine2());
//	          existingPensioner.setPermanentAddressLine3(pensionerDetailsEntity.getPermanentAddressLine3());
//	          existingPensioner.setPermanentDistrict(pensionerDetailsEntity.getPermanentDistrict());
//	          existingPensioner.setPermanentState(pensionerDetailsEntity.getPermanentState());
//	          existingPensioner.setPermanentPincode(pensionerDetailsEntity.getPermanentPincode());
//	          existingPensioner.setTempAddressLine1(pensionerDetailsEntity.getTempAddressLine1());
//	          existingPensioner.setTempAddressLine2(pensionerDetailsEntity.getTempAddressLine2());
//	          existingPensioner.setTempAddressLine3(pensionerDetailsEntity.getTempAddressLine3());
//	          existingPensioner.setTempDistrict(pensionerDetailsEntity.getTempDistrict());
//	          existingPensioner.setTempState(pensionerDetailsEntity.getTempState());
//	          existingPensioner.setTempPincode(pensionerDetailsEntity.getTempPincode());
//	          existingPensioner.setDateOfDeath(pensionerDetailsEntity.getDateOfDeath());
//	          existingPensioner.setRelation(pensionerDetailsEntity.getRelation());
//	          existingPensioner.setEmployeeId(pensionerDetailsEntity.getEmployeeId());
//	          existingPensioner.setEmployeeName(pensionerDetailsEntity.getEmployeeName());
//	        
//	        PensionerDetailsEntity updatedPensioner = pensionerDetailsService.save(existingPensioner);
//	        
//	        ResponseBean response = new ResponseBean();
//	        
//	        response.setPensionerDetailsEntity(updatedPensioner);
//	        response.setMessage("success");
//	        
//	        return ResponseEntity.ok(response);
//	    } else {
//	        // Save as a new entity if it doesn't exist
//	        return (ResponseEntity<ResponseBean>) ResponseEntity.notFound();
//	    }
//	}

	@PostMapping("/getPensionerReport")
	public ResponseEntity<?> getPensionerReport(@RequestBody RequestBean requestBean){
		List<Map<String,Object>> reposne = detailsRepo.getPensionerReport(requestBean.getOfficeId(), requestBean.getPensionerType());
		return ResponseEntity.ok(reposne);
	
	}
}
