package com.twad.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twad.entity.PensionerDetailsEntity;
import com.twad.entity.PensionerEntity;
import com.twad.repo.PensionerDetailsRepo;

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

	public List<PensionerEntity> getEmployeeDetails(int i, String pensionerTypeId) {
		return pensionerDetailsRepo.getEmployeeDetails(i, pensionerTypeId);
	}
//	
//	public List<Map<String, Object>> (Long officeId, String pensionerType) {
//		return pensionerDetailsRepo.getEmployeeDetails(officeId, pensionerType);
//	}
//	


}
