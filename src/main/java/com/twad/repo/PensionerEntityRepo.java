package com.twad.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twad.entity.PensionerDetailsEntity;
import com.twad.entity.PensionerEntity;

@Repository
public interface PensionerEntityRepo extends JpaRepository<PensionerEntity, Integer>{
	
	@Query(value = "From PensionerEntity where paymentOfficeId =:officeId and pensionerTypeId =:pensionerTypeId ")
	List<PensionerEntity> getEmployeeDetails(int officeId, String pensionerTypeId);
	
	







	PensionerEntity findByPpoNo(int ppoNo);








	@Query(value = "select \r\n"
			+ "  ori.pensioner_type_id,"
			+ ""
			+ "case when ori.pensioner_type_id = 'P' THEN 'Pensioners' else 'Family Pensioners' end as pensioner_type, \r\n"
			+ "  count(*) as total, \r\n"
			+ "  count(case when ori.aadhaar_no is distinct from dat.aadhaar_no then 1 end) as updated,\r\n"
			+ "  count(case when ori.aadhaar_no is not distinct from dat.aadhaar_no then 1 end) as not_updated\r\n"
			+ "from twad_pensioner_details_original ori\r\n"
			+ "left join twad_pensioner_details dat \r\n"
			+ "  on dat.ppo_no = ori.ppo_no \r\n"
			+ " and dat.id = ori.id\r\n"
			+ "where ori.payment_office_id = :paymentOfficeId\r\n"
			+ "group by ori.pensioner_type_id; ", nativeQuery = true)
	List<Map<String, Object>> getAadhaarStatusReport(int paymentOfficeId);








	@Query(value = """
		    select
		      ori.ppo_no,
		      ori.id,
		      ori.name,
		      ori.pensioner_type_id,
		      case when ori.pensioner_type_id = 'P' THEN 'Pensioners' else 'Family Pensioners' end as pensioner_type,
		      ori.aadhaar_no as original_aadhaar_no,
		      dat.aadhaar_no as current_aadhaar_no,
		      case
		        when ori.aadhaar_no is distinct from dat.aadhaar_no then 'UPDATED'
		        when ori.aadhaar_no is not distinct from dat.aadhaar_no then 'NOT_UPDATED'
		      end as status
		    from twad_pensioner_details_original ori
		    left join twad_pensioner_details dat
		      on dat.ppo_no = ori.ppo_no
		     and dat.id = ori.id
		    where ori.payment_office_id = :paymentOfficeId
		      and ori.pensioner_type_id = :pensionerTypeId
		      and (
		          (:status = 'UPDATED' and ori.aadhaar_no is distinct from dat.aadhaar_no)
		       or (:status = 'NOT_UPDATED' and ori.aadhaar_no is not distinct from dat.aadhaar_no)
		      )
		    order by ori.name
		    """, nativeQuery = true)
		List<Map<String, Object>> getAadhaarStatusDetailed(
		        @Param("paymentOfficeId") int paymentOfficeId,
		        @Param("pensionerTypeId") String pensionerTypeId,
		        @Param("status") String status
		);








	@Query(value = "select\r\n"
		+ "  ori.payment_office_id,\r\n"
		+ "  ori.payment_office_name,\r\n"
		+ "  ori.pensioner_type_id,\r\n"
		+ "  case when ori.pensioner_type_id = 'P' THEN 'Pensioners' else 'Family Pensioners' end as pensioner_type,\r\n"
		+ "  count(*) as total,\r\n"
		+ "  count(case when ori.aadhaar_no is distinct from dat.aadhaar_no then 1 end) as updated,\r\n"
		+ "  count(case when ori.aadhaar_no is not distinct from dat.aadhaar_no then 1 end) as not_updated\r\n"
		+ "from twad_pensioner_details_original ori\r\n"
		+ "left join twad_pensioner_details dat\r\n"
		+ "  on dat.ppo_no = ori.ppo_no\r\n"
		+ " and dat.id = ori.id\r\n"
		+ "group by ori.payment_office_id, ori.payment_office_name, ori.pensioner_type_id\r\n"
		+ "order by ori.payment_office_name, ori.pensioner_type_id;", nativeQuery = true)
	List<Map<String, Object>> getAadhaarStatusReportAllOffices();







//
//	@Query(value = "select * from twad_pensioner_details where payment_office_id = :paymentOfficeId and pensioner_type_id=:pensionerTypeId "
//			+ "and  "
//	List<Map<String, Object>> getAadhaarStatusDetailed(int paymentOfficeId, String pensionerTypeId)
//	;

}
