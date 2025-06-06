package com.eodb.eodb_tariff.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eodb.eodb_tariff.bean.RequestBean;
import com.eodb.eodb_tariff.dto.BeneficiaryDetailsDTO;
import com.eodb.eodb_tariff.repo.HomeRepository;
import com.eodb.eodb_tariff.service.HashkeyValidationService;
import com.eodb.eodb_tariff.service.HomeService;

@RestController
@RequestMapping("/dcbData")
public class HomeController {
	
	@Autowired
	private HomeRepository homeRepository;
	
	@Autowired
	private HashkeyValidationService hashkeyValidationService;
	
	@Autowired
	private HomeService homeService;
	
	
	@PostMapping()
    public List<BeneficiaryDetailsDTO> getData(@RequestBody RequestBean request) {
       int[] beneficiaries = request.getBeneficiaries();
        Date receiptDate = request.getDate();
        String hashkey = request.getHashkey();

        if (!hashkeyValidationService.validateHashkey(hashkey)) {
            return null;
        }

        List<BeneficiaryDetailsDTO> response = homeService.getSrnEodbMonthlyTariff(beneficiaries, receiptDate);

        return response;
    }
	

}
