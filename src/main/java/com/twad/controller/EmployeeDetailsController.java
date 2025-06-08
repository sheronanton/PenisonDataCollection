package com.twad.controller;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twad.bean.*;
import java.util.Date;
import java.util.HashMap;

import com.twad.entity.PensionerDetailsEntity;
import com.twad.entity.PensionerEntity;
import com.twad.repo.PensionerDetailsRepo;
import com.twad.repo.PensionerEntityRepo;
import com.twad.service.PensionerDetailsService;

@RestController
@RequestMapping("employees")
@CrossOrigin
public class EmployeeDetailsController {
	
	
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	@Autowired
	private PensionerDetailsService pensionerDetailsService;
	
	@Autowired
	private PensionerDetailsRepo detailsRepo;
	
	@Autowired
	private PensionerEntityRepo pensionerEntityRepo ;
	
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
	public List<PensionerEntity> getEmployeeDetails(@RequestBody PensionerBean bean) {
		
		String pensionerTypeId  = (String) bean.getPensionerTypeId();
		
		
		
		List<PensionerEntity> pensionDetails =pensionerDetailsService.getEmployeeDetails(bean.getPaymentOfficeId(), pensionerTypeId);



		return pensionDetails;
	}
	

	@Transactional
	@PostMapping("/savePensionerDetails")
	public ResponseEntity<ResponseBean> savePensionerDetails(@RequestBody PensionerBean bean) {
	    try {
	        // Debug print all fields
	        for (Field field : bean.getClass().getDeclaredFields()) {
	            field.setAccessible(true);
	            System.out.println(field.getName() + " = " + field.get(bean));
	        }
	    } catch (IllegalAccessException e) {
	        e.printStackTrace();
	    }

	    // Fetch existing pensioner by PPO No, or create a new one
	    PensionerEntity pensioner = pensionerEntityRepo.findByPpoNo(bean.getPpoNo());
	    if (pensioner == null) {
	        pensioner = new PensionerEntity(); // or throw error if PPO must exist
	    }

	    // Map data from bean to entity
	    pensioner.setPpoNo(bean.getPpoNo());
	    pensioner.setName(bean.getName());
	    pensioner.setGender(bean.getGender());
	    pensioner.setDateOfBirth(bean.getDateOfBirth());
	    pensioner.setBankCode(bean.getBankCode());
	    pensioner.setBankAccount(bean.getBankAccount());
	    pensioner.setBankName(bean.getBankName());
	    pensioner.setBranchName(bean.getBranchName());
	    pensioner.setPensionerTypeId(bean.getPensionerTypeId());
	    pensioner.setPaymentOfficeId(bean.getPaymentOfficeId());
	    pensioner.setPaymentOfficeName(bean.getPaymentOfficeName());
	    pensioner.setMobileNumber(bean.getMobileNumber());
	    pensioner.setEmail(bean.getEmail());
	    pensioner.setPanNo(bean.getPanNo());
	    pensioner.setAadhaarNo(bean.getAadhaarNo());
	    pensioner.setAadhaarStatus(bean.getAadhaarStatus());
	    pensioner.setId(bean.getId()); // if you allow ID to be updated
	    pensioner.setLifeCertificate(bean.getLifeCertificate());
	    pensioner.setReMarriage(bean.getReMarriage());
	    pensioner.setReEmployed(bean.getReEmployed());
	    pensioner.setAuthenticationDate(bean.getAuthenticationDate());
	    pensioner.setPramaanId(bean.getPramaanId());
	    pensioner.setPensionerType(bean.getPensionerType());
	    pensioner.setAddressLine1(bean.getAddressLine1());
	    pensioner.setAddressLine2(bean.getAddressLine2());
	    pensioner.setAddressLine3(bean.getAddressLine3());
	    pensioner.setState(bean.getState());
	    pensioner.setDistrict(bean.getDistrict());
	    pensioner.setPincode(bean.getPincode());
	    pensioner.setUpdatedBy(bean.getUserName());
	    pensioner.setFlag(bean.getFlag());
	    


	    // ... map all other fields as needed

	    // Save to DB
	    pensionerEntityRepo.save(pensioner);
	    updatePensionMaster(bean);

	    return ResponseEntity.ok(new ResponseBean());
	}
	
	
	public String updatePensionMaster(PensionerBean bean) {
		
		String updateSql="";
		
		if (bean.getPensionerTypeId().equalsIgnoreCase("P")) {
			 updateSql = """
				    UPDATE hrm_pen_mst_address
				    SET
				        aadhar_no = :aadhaarNo,
				        pan_no = :panNo,
				        contact_cell = :mobileNumber,
				        updated_user_id = :updatedBy,
				        updated_date = now()
				    WHERE ppo_no = :ppoNo
				""";
			
		}else if (bean.getPensionerTypeId().equalsIgnoreCase("F")    ) {
			 updateSql = """
					    UPDATE HR_PEN_MST_FAMILY_ADDRESS
					    SET
					        aadhar_no = :aadhaarNo::NUMERIC,
					        pan_no = :panNo,
					        contact_cell = :mobileNumber,
					        updated_user_id = :updatedBy,
					        updated_date = now()
					    WHERE ppo_no = :ppoNo
					""";
		}
		
	

			Map<String, Object> params = new HashMap<>();
			params.put("ppoNo", bean.getPpoNo());
			try {
			    params.put("aadhaarNo", 
			        (bean.getAadhaarNo() != null && !bean.getAadhaarNo().isBlank()) 
			            ? Long.parseLong(bean.getAadhaarNo()) 
			            : null
			    );
			} catch (NumberFormatException e) {
			    params.put("aadhaarNo", null);
			}
			params.put("panNo", bean.getPanNo());
			params.put("mobileNumber", bean.getMobileNumber());
			params.put("pensionerType", bean.getPensionerType());
			params.put("updatedBy", bean.getUserName());

			int rowsAffected = namedJdbcTemplate.update(updateSql, params);
			System.out.println("Master table updated rows: " + rowsAffected);
			
			return updateSql;
		
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
