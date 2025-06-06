package com.twad.repo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.twad.entity.CheckData;

import jakarta.transaction.Transactional;

@Repository
public interface CheckDataRepository extends JpaRepository <CheckData,Long> {

	
	
	@Query(value="SELECT\r\n"
			+ "	op1.beneficiary_sno  as rd_beneficiary_sno ,\r\n"
			+ "	op1.district_name as rd_district_name ,\r\n"
			+ "	op1.block_name as rd_block_name,\r\n"
			+ "	op1.village_name as rd_village_name, \r\n"
			+ "	pms.beneficiary_sno ,\r\n"
			+ "	cd.district_name,\r\n"
			+ "	cb.block_name,\r\n"
			+ "	cp.panch_name ,\r\n"
			+ "	op1.offices_name\r\n"
			+ "FROM\r\n"
			+ "	(\r\n"
			+ "	SELECT\r\n"
			+ "		rd.beneficiary_sno,\r\n"
			+ "		rd.offices_id,\r\n"
			+ "		rd.offices_name,\r\n"
			+ "		rd.district_name,\r\n"
			+ "		rd.block_name,\r\n"
			+ "		rd.village_name \r\n"
			+ "	FROM\r\n"
			+ "		rd_transaction_temp rd \r\n"
			+ "	WHERE\r\n"
			+ "		beneficiary_sno NOT IN ( SELECT beneficiary_sno FROM rd_transaction  GROUP BY beneficiary_sno ) \r\n"
			+ "	GROUP BY\r\n"
			+ "		beneficiary_sno,\r\n"
			+ "		rd.offices_id,\r\n"
			+ "		rd.offices_name,\r\n"
			+ "		rd.district_name,\r\n"
			+ "		rd.block_name,\r\n"
			+ "		rd.village_name \r\n"
			+ "	) op1\r\n"
			+ "	LEFT JOIN pms_dcb_mst_beneficiary pms ON op1.beneficiary_sno = pms.beneficiary_sno\r\n"
			+ "	LEFT JOIN com_mst_blocks cb ON cb.block_sno = pms.block_sno\r\n"
			+ "	LEFT JOIN com_mst_districts cd ON cd.district_code = pms.district_code\r\n"
			+ "	LEFT JOIN com_mst_panchayats cp ON cp.panch_sno = pms.village_panchayat_sno",nativeQuery=true)
	
		List<Map<String, Object>> CheckUploadedData();
	
	
		@Query(value= "select count(*) from rd_transaction where receipt_date = :receiptDate  ",nativeQuery=true )
		int checkDate(Date receiptDate);
		
		@Query(value= "select count(*) from rd_transaction where  upi_id in (select upi_id from rd_transaction_temp)  ",nativeQuery=true )
		int checkUTR();


		
		@Query(value="select * from fas_rd_reverted_data where verify= :status order by receipt_date",nativeQuery=true)
		List<Map<String, Object>> getVerifyData(String status);
		
		
		@Modifying
		@Transactional
		@Query(value="update fas_rd_reverted_data rd set verify='Y' where rd.beneficiary_sno = :beneficiary_sno ::numeric and receipt_date = :date ::date",nativeQuery=true)
		int verifyData(String beneficiary_sno,String date);


		@Query(value="select * from ((select sum(receipt_amount) as rd_sum,receipt_date from rd_transaction where receipt_date between :fromDate and :toDate group by receipt_date order by receipt_date)op1\r\n"
				+ "left join \r\n"
				+ "(select * from (\r\n"
				+ "select sum(a.total_amount) as rec_ms,sum(b.amount) as rec_trans,a.receipt_date  from fas_receipt_master a left join fas_receipt_transaction b on  a.accounting_unit_id =b.accounting_unit_id and a.accounting_for_office_id=b.accounting_for_office_id and a.cashbook_year=b.cashbook_year and a.cashbook_month=b.cashbook_month and a.receipt_no = b.receipt_no where  a.receipt_no in (select receipt_no from rd_final_data where process_date between  :fromDate and :toDate group by process_date,receipt_no) and a.receipt_date between  :fromDate and :toDate and a.accounting_unit_id=5 and a.receipt_status='L' group by a.receipt_date  order by a.receipt_date)op)op2 on op2.receipt_date=op1.receipt_date\r\n"
				+ "left join \r\n"
				+ "(select sum(a.total_amount) as adj_ms,sum(b.amount) as adj_trans,a.voucher_date  from fas_adjust_memo_mst a left join fas_adjust_memo_trn b on  a.accounting_unit_id =b.accounting_unit_id and a.accounting_for_office_id=b.accounting_for_office_id and a.cashbook_year=b.cashbook_year and a.cashbook_month=b.cashbook_month and a.voucher_no = b.voucher_no where  a.voucher_no in (select adj_voucher_no from rd_final_data where process_date between  :fromDate and :toDate group by process_date,adj_voucher_no) and a.voucher_date between  :fromDate and :toDate and a.accounting_unit_id=5 and  a.memo_status = 'L' group by a.voucher_date  order by a.voucher_date)op3 on op3.voucher_date=op1.receipt_date \r\n"
				+ "left join \r\n"
				+ "(select sum(fj.amount) as journal_sum,rd.process_date from  fas_journal_transaction fj  left join rd_final_data rd on rd.accounting_for_office_id =fj.accounting_for_office_id and rd.cashbook_year=fj.cashbook_year and rd.cashbook_month=fj.cashbook_month and rd.journal_no=fj.voucher_no and fj.cb_ref_no=rd.adj_voucher_no  and fj.sub_ledger_code= rd.beneficiary_sno where   fj.particulars like '%WC received through TN-Pass%' and  rd.process_date between  :fromDate and :toDate group by rd.process_date order by process_date)op4 on op4.process_date=op1.receipt_date \r\n"
				+ "left join \r\n"
				+ "(select to_date(voucher_date,'dd/mm/yyyy') as voucher_date ,sum(fj.amount)as other_charge from  pms_dcb_other_charges fj where  particulars like '%Adjustment of WC through TNPASS%' and to_char(to_date(voucher_date,'dd/mm/yyyy'),'yyyy-mm-dd')::date between :fromDate and :toDate group by voucher_date order by voucher_date)op5 on op5.voucher_date=op1.receipt_date \r\n"
				+ "left join \r\n"
				+ "(select sum(wc_coln)+sum(int_coln) as final_sum,process_date  from rd_final_data where process_date between  :fromDate and :toDate group by process_date  order by process_date)op6 on op6.process_date\r\n"
				+ "=op1.receipt_date \r\n"
				+ "left join \r\n"
				+ "(Select sum(coalesce(wc_coln,0)) + sum(coalesce(int_coln,0)) +sum(coalesce(maint_coln,0)) as scheme_wise_sum,receipt_date   from fas_rd_beneficiary_schemewise_balance  where receipt_date between :fromDate and :toDate group by receipt_date order by receipt_date)op7 on op7.receipt_date =op1.receipt_date )opt\r\n"
				+ "",nativeQuery=true)
		
		List<Map<String, Object>> getCalculateData(java.sql.Date fromDate, java.sql.Date toDate);
		
		
		@Query(value="Select sum(coalesce(wc_coln,0)) + sum(coalesce(int_coln,0)) +sum(coalesce(maint_coln,0)) as scheme_wise_sum,fd.receipt_date,fd.beneficiary_sno,rd.district_name,rd.block_name,rd.village_name,rd.offices_name   from fas_rd_beneficiary_schemewise_balance  fd left join rd_transaction rd  on rd.beneficiary_sno=fd.beneficiary_sno and rd.receipt_date =fd.receipt_date where fd.receipt_date =:fromDate group by fd.receipt_date,fd.beneficiary_sno,rd.district_name,rd.block_name,rd.village_name,rd.offices_name order by fd.receipt_date,fd.beneficiary_sno",nativeQuery=true)
		
		List<Map<String, Object>> getSelectedData(java.sql.Date fromDate);
}
