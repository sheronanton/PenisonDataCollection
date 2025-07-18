package com.twad.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.twad.entity.PensionerDetailsEntity;
import com.twad.entity.PensionerEntity;

@Repository
public interface PensionerDetailsRepo extends JpaRepository<PensionerDetailsEntity,Integer>{

	
	@Query(value = "SELECT"
			+ "	pen.ppo_no,"
			+ "	'Pensioner' AS pensioner_type,"
			+ "	pensioner_initial,"
			+ "	pensioner_name,"
			+ "	pen.designation_id,"
			+ "	des.designation,"
			+ "	date_of_birth,"
			+ "	date_of_retirement,"
			+ "	sex AS gender,"
			+ " coalesce(nullif(pan_number,'') ) pan_number , "
			+ "	NULL AS aadhar_no,"
			+ "	coalesce(nullif(contact_cell,'') ) contact_cell,"
			+ "	coalesce(nullif(contact_landline,'') ) contact_landline,"
			+ "	addr.contact_email_id,"
			+ "	addr.address,"
			+ "	pen.payment_office_id,"
			+ "	OFF.office_name,"
			+ "	pay.gross_pay as gross_pay, "
			+ "	pay.net_pay as net_pay ,"
			+ "	addr.address as permanent_address_line1, "
			+ "	null as permanent_address_line2, "
			+ "	null as permanent_address_line3, "
			+ "	addr.district as permanent_district , "
			+ "	st.state_name as permanent_state, "
			+ "	addr.pincode as permanent_pinocode, "
			+ "	null as temp_address_line1, "
			+ "	null as temp_address_line2, "
			+ "	null as temp_address_line3, "
			+ "	null as  temp_district, "
			+ "	null as  temp_state, "
			+ "	null as  temp_pincode"
			+ " FROM"
			+ "	hrm_pen_mst_details pen"
			+ "	LEFT JOIN hrm_pen_mst_address addr ON addr.ppo_no = pen.ppo_no"
			+ "	LEFT JOIN hrm_mst_designations des ON des.designation_id = pen.designation_id"
			+ "	LEFT JOIN com_mst_offices OFF ON OFF.office_id = pen.payment_office_id "
			+ " Left join (select ppo_no , gross_pay , net_pay from hrm_pen_pay_final where for_month_from = '8' and for_year_from = '2024') pay on pay.ppo_no = pen.ppo_no "
			+ " Left join com_mst_state st on st.state_code = cast(addr.state as numeric) "
			+ " WHERE"
			+ "	pension_status = 'false' and pen.payment_office_id = :officeId order by pensioner_name;", nativeQuery = true)
	List<Map<String, Object>> getEmployeeDetails1(Long officeId);
	
	
	
	
	
	
	@Query(value = " SELECT "
			+ "	 fam.ppo_no, "
			+ "	'F' AS pensioner_type, "
			+ "	fpensioner_initial as pensioner_initial, "
			+ "	fpensioner_name as pensioner_name, "
			+ "	fam.date_of_birth	, "
			+ "	 sex as gender , "
			+ "	addr.address as permanent_address_line1, "
			+ "	addr.district as permanent_district, "
			+ "	st.state_name as permanent_state, "
			+ "	addr.pincode as permanent_pincode, "
			+ "	null as temp_address_line1, "
			+ "	null as temp_district, "
			+ "	null as temp_state,  "
			+ "	null as temp_pincode, "
			+ "	emp_det.employee_id, "
			+ "	doj.doj as emp_date_of_joining, "
			+ "	fam.date_of_death as emp_date_of_death, "
			+ "	fam.date_of_retirement as emp_date_of_retirement, "
			+ "	des.designation, "
			+ " CASE WHEN fam.relation_id=1 THEN 'Spouse' WHEN fam.relation_id=2 THEN 'Son' WHEN fam.relation_id=3 THEN 'Daughter' WHEN fam.relation_id=4 THEN 'Father' WHEN fam.relation_id=5 THEN 'Mother' END AS Relation "
			+ " FROM "
			+ "	hr_pen_mst_family fam left join HR_PEN_MST_FAMILY_ADDRESS addr on addr.ppo_no = fam.ppo_no "
			+ "	left join HR_PEN_MST_FAMILY_EMP_DET emp_det on emp_det.ppo_no = fam.ppo_no "
			+ "	left join hrm_emp_current_posting cp on cp.employee_id = emp_det.employee_id "
			+ "	left join doj_view doj on doj.employee_id = emp_det.employee_id "
			+ "	left join allretirementview ret on ret.employee_id = emp_det.employee_id "
			+ "	left join hrm_mst_designations des on des.designation_id = emp_det.emp_desig_id "
			+ " Left join com_mst_state st on st.state_code = cast(addr.state as numeric) "
			+ " WHERE "
			+ "	fam.pension_status = 'false'  "
			+ "	AND fam.payment_office_id = :officeId  "
			+ " ORDER BY "
			+ "	ppo_no; "
			+ "	", nativeQuery = true)
	List<Map<String, Object>> getFamilyPensionerDetails(Long officeId);
	
//	@Query(value = " SELECT "
//			+ "	*  from twad_pensioner_details where payment_office_id = :officeId and pensioner_type= :pensionerType ORDER BY FLAG ", nativeQuery = true)
//	List<Map<String, Object>> getEmployeeDetails(int officeId, String pensionerType);
	
	
	@Query("""
		    FROM PensionerEntity 
		    WHERE paymentOfficeId = :officeId 
		      AND pensionerTypeId = :pensionerTypeId
		    ORDER BY aadhaarStatus NULLS FIRST, aadhaarStatus
		""")
		List<PensionerEntity> getEmployeeDetails(int officeId, String pensionerTypeId);
	
	
//	@Query(value = """
//		    SELECT * 
//		    FROM pensioner_entity
//		    WHERE payment_office_id = :officeId 
//		      AND pensioner_type_id = :pensionerTypeId
//		    ORDER BY
//		      CASE
//		        WHEN aadhaar_status IS NULL THEN 1
//		        WHEN aadhaar_status = 'INVALID AADHAAR' THEN 2
//		        WHEN aadhaar_status = 'VALID AADHAAR' THEN 3
//		        ELSE 4
//		      END
//		""", nativeQuery = true)
//		List<PensionerEntity> getEmployeeDetails(int officeId, String pensionerTypeId);

	
	
	@Query(value = " SELECT * from pensioner_details where payment_office_id = :officeId and pensioner_type= :pensionerType ORDER BY name ", nativeQuery = true)
	List<Map<String,Object>> getPensionerReport(Long officeId, String pensionerType);






	PensionerDetailsEntity findByPpoNo(int ppoNo);

}
