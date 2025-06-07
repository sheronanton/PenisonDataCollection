package com.twad.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.twad.entity.PensionerDetailsEntity;
import com.twad.entity.PensionerEntity;

public interface PensionerEntityRepo extends JpaRepository<PensionerEntity, Integer>{
	
	@Query(value = "From PensionerEntity where paymentOfficeId =:officeId and pensionerTypeId =:pensionerTypeId ")
	List<PensionerEntity> getEmployeeDetails(int officeId, String pensionerTypeId);
	
	







	PensionerEntity findByPpoNo(int ppoNo);

}
