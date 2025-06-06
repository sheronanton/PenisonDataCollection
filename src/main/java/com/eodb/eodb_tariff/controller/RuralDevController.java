package com.eodb.eodb_tariff.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eodb.eodb_tariff.bean.RequestBean;
import com.eodb.eodb_tariff.bean.ResponseBean;
import com.eodb.eodb_tariff.entity.CheckData;
import com.eodb.eodb_tariff.entity.RuralDevData;
import com.eodb.eodb_tariff.entity.VerifyRuralDevData;
import com.eodb.eodb_tariff.repo.CheckDataRepository;
import com.eodb.eodb_tariff.repo.RuralDevRepository;
import com.eodb.eodb_tariff.service.RuralDevService;



@RestController
@RequestMapping("/rd")
public class RuralDevController {
	
	@Autowired
	RuralDevService ruralService;
	@Autowired
	RuralDevRepository ruralRepository;
	
	@Autowired
	CheckDataRepository checkRepo;
	

	
	@PostMapping("/data")
	public String saveRuralData(@RequestBody RuralDevData data) {
		
		 ruralService.saveRuralData(data);
		 return "success";
		
	}
	
	
	@PostMapping("/dataVerify")
	public String saveVerifyData(@RequestBody VerifyRuralDevData data) {
		
		 ruralService.saveVerifyData(data);
		 return "success";
		
	}
	
	@PostMapping("/dataChk")
		public String saveCheckData(@RequestBody CheckData data) {
			
			 ruralService.saveCheckData(data);
			 return "success";
			
		}
	
	
	
	
	@PostMapping("/generate")
	public ResponseEntity<ResponseBean> generateReceipt(@RequestBody RequestBean request) {
		
		ResponseBean resp = new ResponseBean();
		
		Date receiptDate=request.getRdReceiptDate();
		
//		ruralRepository.transferData(receiptDate);
		
		resp.setMessage(ruralService.generateReceipt(receiptDate,"VP"));
//		resp.setMessage(ruralService.generateReceipt(receiptDate));
		
		//System.out.print(status);
		 return ResponseEntity.ok().body(resp);
	}
	
	
	
	
//	@PostMapping("/checkdate")
//	public int checkDate(@RequestBody RequestBean request) {
//		
//		Date receiptDate=request.getRdReceiptDate();
//		//String upiId=request.getUpiId();
//		
//		int count= ruralRepository.checkDate(receiptDate);
//		System.out.println("count"+count);
//	
//		if(count>=1) {
//			return 0;
//		}
//		else
//		{
//			return 1;
//		}
//	}
	
	@PostMapping("/generateStatusReport")
	public ResponseEntity<ResponseBean> generateReport(@RequestBody RequestBean request) {
		
		ResponseBean resp = new ResponseBean();
		
		Date receiptDate = request.getRdReceiptDate();
		String flag = request.getFlag();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		
		if(flag.equalsIgnoreCase("scheme") ) {
			 result = ruralService.generateReportByScheme(receiptDate);

		}else if (flag.equalsIgnoreCase("beneficiary")) {
			 result = ruralService.generateReportByBeneficiary(receiptDate);
		}
		
		resp.setResult(result);

		return ResponseEntity.ok().body(resp);
		
	};
	
	@PostMapping("/cancelUpload")
	public String cancelUpload(@RequestBody RequestBean request) {
		
		Date receiptDate = request.getRdReceiptDate();
		 ruralService.cancelUpload(receiptDate);
		 return "cancelled";
	}

	
	@PostMapping("/dmdReport")
	public List<Map<String,Object>> getDmdReport(@RequestBody RequestBean request){
		
		
		return ruralRepository.getDmdReport(request.getDemandYear(),request.getDemandMonth());
	}
	
	
	@PostMapping("/importedData")
	public List<Map<String,Object>> getImportedData(@RequestBody RequestBean request){
		Date receiptDate = request.getRdReceiptDate();
		return ruralService.getImportedData(receiptDate);
	}
	
	@PostMapping("/dataProcess")
	public ResponseBean CheckUploadedData(@RequestBody RequestBean request){
		
		String str=request.getChkString();
		ResponseBean response=new ResponseBean();
		
		if(str.equalsIgnoreCase("checkDate")) {
			
			int count =checkRepo.checkDate(request.getRdReceiptDate());
			response.setCount(count);

		}
		
		if(str.equalsIgnoreCase("checkUTR")) {
			int count =checkRepo.checkUTR();
			if (count == 0) {
				response.setCheckedData(checkRepo.CheckUploadedData());
			}
			else
			{
				response.setCount(count);
			}
			
		}
		
		return response;
		
	}
	
	
	@PostMapping ("/getVerifyData")
	public List<Map<String,Object>> getVerifyData(@RequestBody RequestBean request){
		List<Map<String,Object>> verifyData =null;
		String str=request.getFlag();
		String status=request.getStatus();
		if(str.equalsIgnoreCase("get")) {
			
			verifyData=checkRepo.getVerifyData(status);
		}
		return verifyData;
		
	}
	
	@PostMapping("/verifyData")
	public String  getVerify(@RequestBody RequestBean request){
		
		Map<String,String> data=request.getVerifyData();
 
           if (data.get("status").equalsIgnoreCase("Y")) {
        	   
        	   checkRepo.verifyData(data.get("ben_sno"),data.get("date"));
    } 
		//checkRepo.verifyData();
		return "verified";
	}
	
	@PostMapping("/calculateData")
	public List<Map<String,Object>> getCalculateData(@RequestBody RequestBean request){
		
		List<Map<String,Object>> res = checkRepo.getCalculateData(request.getFromDate(),request.getToDate());
		return checkRepo.getCalculateData(request.getFromDate(),request.getToDate());
	}
		
	@PostMapping("/getSelectedData")
	public List<Map<String,Object>> getSelectedData(@RequestBody RequestBean request){
		return checkRepo.getSelectedData(request.getFromDate());
	}
}
