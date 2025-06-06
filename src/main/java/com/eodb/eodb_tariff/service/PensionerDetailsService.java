package com.eodb.eodb_tariff.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eodb.eodb_tariff.entity.PensionerDetailsEntity;
import com.eodb.eodb_tariff.repo.PensionerDetailsRepo;

@Service
public class PensionerDetailsService {
	
	@Autowired
	private PensionerDetailsRepo pensionerDetailsRepo;
	
	public PensionerDetailsEntity save(PensionerDetailsEntity pensionerDetailsEntity) {
		return pensionerDetailsRepo.save(pensionerDetailsEntity);
	}

	public PensionerDetailsEntity findByPpoNo(int ppoNo) {
		// TODO Auto-generated method stub
		return pensionerDetailsRepo.findByPpoNo(ppoNo);
	}

	public List<Map<String, Object>> getEmployeeDetails(int officeId, String pensionerType) {
		return pensionerDetailsRepo.getEmployeeDetails(officeId, pensionerType);
	}
//	
//	public List<Map<String, Object>> (Long officeId, String pensionerType) {
//		return pensionerDetailsRepo.getEmployeeDetails(officeId, pensionerType);
//	}
//	


}
