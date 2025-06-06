package com.eodb.eodb_tariff.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eodb.eodb_tariff.entity.CheckData;
import com.eodb.eodb_tariff.entity.RuralDevData;
import com.eodb.eodb_tariff.entity.VerifyRuralDevData;

public interface RuralDevService {

	public RuralDevData saveRuralData(RuralDevData data);

	public String generateReceipt(Date receiptDate,String flag);
//	public String generateReceipt(Date receiptDate);

	public  List<Map<String, Object>>  generateReportByScheme(Date receiptDate);
	
	public  List<Map<String, Object>>  generateReportByBeneficiary(Date receiptDate);

	public int cancelUpload(Date receiptDate);

	public List<Map<String, Object>> getImportedData(Date receiptDate);

	public CheckData saveCheckData(CheckData data);

	public VerifyRuralDevData saveVerifyData(VerifyRuralDevData data);


}
