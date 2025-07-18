package com.twad.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.twad.bean.RequestBean;
import com.twad.entity.DCBCollection;
import com.twad.entity.ReportData;
import com.twad.service.ReportService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/employees/report")
public class ReportController {

	@Autowired
	private ReportService reportService;

	

	@GetMapping("/panchayatDetails")
	public List<Map<String, Object>> generatePanchayat() {

		return reportService.executeReportQuery();
	}


	@PostMapping("/GetAadhaarStatusAbstract")
    public ResponseEntity<?> getAadhaarStatusReport(
            @RequestBody RequestBean req) {
        return ResponseEntity.ok(
            reportService.getAadhaarStatusReport(req.getPaymentOfficeId())
        );
    }

	@PostMapping("/GetAadhaarStatusDetailed")
    public ResponseEntity<?> getAadhaarStatusDetailed(
            @RequestBody RequestBean req) {
        return ResponseEntity.ok(
            reportService.getAadhaarStatusDetailed(req.getPaymentOfficeId(),req.getPensionerTypeId() ,req.getStatus()) );
        
    }
	
	
	@PostMapping("/GetAadhaarStatusAbstractAllOffices")
    public ResponseEntity<?> getAadhaarStatusReportAllOffices(
            @RequestBody RequestBean req) {
        return ResponseEntity.ok(
            reportService.getAadhaarStatusReportAllOffices()
        );
    }

}
