package com.eodb.eodb_tariff.repo;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eodb.eodb_tariff.entity.BeneficiaryModel;

@Repository
public interface HomeRepository extends JpaRepository<BeneficiaryModel, Long>{
	
	

	    @Query(value = "SELECT * FROM srn_eodb_monthly_tariff(:p_beneficiary_sno, :p_date)", nativeQuery = true)
	    List<Map<String, Object>> callSrnEodbMonthlyTariff(int[] p_beneficiary_sno, Date p_date);
	
	    @Query(value = "SELECT * FROM eodb_monthly_tariff_data", nativeQuery = true)
	    List<Map<String, Object>>  getMonthlyTariff();
	
}
