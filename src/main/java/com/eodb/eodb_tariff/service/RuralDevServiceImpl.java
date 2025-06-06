package com.eodb.eodb_tariff.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.eodb.eodb_tariff.entity.CheckData;
import com.eodb.eodb_tariff.entity.RuralDevData;
import com.eodb.eodb_tariff.entity.VerifyRuralDevData;
import com.eodb.eodb_tariff.repo.CheckDataRepository;
import com.eodb.eodb_tariff.repo.RuralDevRepository;
import com.eodb.eodb_tariff.repo.VerifyDataRepository;

@Service
public class RuralDevServiceImpl implements RuralDevService{
	
	@Autowired
	private RuralDevRepository ruralRepo;
	
	@Autowired
	private CheckDataRepository checkRepo;
	
	@Autowired
	private VerifyDataRepository verifyRuralRepo;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	

	


	@Override
	public RuralDevData saveRuralData(RuralDevData data) {
		// TODO Auto-generated method stub
		return ruralRepo.save(data);
	}


	@Override
	public CheckData saveCheckData(CheckData data) {
		// TODO Auto-generated method stub
		return checkRepo.save(data);
	}
	

	@Override
	public VerifyRuralDevData saveVerifyData(VerifyRuralDevData data) {
		// TODO Auto-generated method stub
		return verifyRuralRepo.save(data);
	}


	@Override
	public String generateReceipt(Date receiptDate,String flag) {
		
		 java.sql.Date date = new java.sql.Date(receiptDate.getTime());
		 
	        
	        // Execute the stored procedure with the parameter
		 jdbcTemplate.update("CALL fas_rd_flag_data(?,?)",flag,date);
	     jdbcTemplate.update("CALL fas_rd_data_generation_copy1(?,?)", date,"VP");
	             
		return "completed";

	}

//	@Override
//	public String generateReceipt(Date receiptDate) {
//		
//		 java.sql.Date date = new java.sql.Date(receiptDate.getTime());
//	        
//	        // Execute the stored procedure with the parameter
//	     jdbcTemplate.update("CALL fas_rd_data_generation(?)", date);
//	             
//		return "completed";
//
//	}





	@Override
	public List<Map<String, Object>> generateReportByScheme(Date receiptDate) {
		return ruralRepo.generateReportByScheme(receiptDate);
		
	}






	@Override
	public List<Map<String, Object>> generateReportByBeneficiary(Date receiptDate) {
		// TODO Auto-generated method stub
		return ruralRepo.generateReportByBeneficiary(receiptDate);
	}






	@Override
	public int cancelUpload(Date receiptDate) {
		// TODO Auto-generated method stub
		return ruralRepo.cancelUpload(receiptDate);
	}






	@Override
	public List<Map<String, Object>> getImportedData(Date receiptDate) {
		// TODO Auto-generated method stub
		return ruralRepo.getImportedData(receiptDate);
	}








	
	

}
