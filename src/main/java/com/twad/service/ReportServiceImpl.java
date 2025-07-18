package com.twad.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.twad.entity.DCBCollection;
import com.twad.entity.ReportData;
import com.twad.repo.PensionerEntityRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class ReportServiceImpl implements ReportService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PensionerEntityRepo pensionerEntityRepo;
	

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ReportData> generateMonthEndBalanceReport(int year, int month) {

		String sql = "select OFF.OFFICE_NAME, A.office_id, E.DISTRICT_CODE, E.DISTRICT_NAME, A.BENEFICIARY_SNO, B.beneficiary_name, A.OB_WC, "
				+ "A.COLN_WC as COLN_WC, A.OB_WC - A.COLN_WC as balance_wc, A.dmd_wc AS DMD_WC, "
				+ "A.OB_WC - A.COLN_WC + A.dmd_wc as Total_wc, "
				+ "A.OB_int, A.COLN_int, A.OB_int - A.COLN_int as balance_int, A.dmd_int, A.OB_int - A.COLN_int + A.dmd_int as Total_int, "
				+ "A.OB_WC - A.COLN_WC + A.dmd_wc + A.OB_int - A.COLN_int + A.dmd_int as Total "
				+ "from (select OFFICE_ID, beneficiary_sno, "
				+ "coalesce(ob_yester_yr_wc,0) + coalesce(ob_cur_yr_wc,0) + coalesce(ob_maint_charges,0) as OB_WC, "
				+ "coalesce(coln_cur_yr_wc,0) + coalesce(coln_yester_yr_wc,0) + coalesce(coln_maint,0) - coalesce(add_charges_wc,0) + coalesce(minus_charges_wc,0) "
				+ "- coalesce(add_charges_maint,0) + coalesce(minus_charges_maint,0) as COLN_WC, "
				+ "coalesce(wc_mth_total,0) as DMD_WC, coalesce(ob_int_amt_wc,0) as OB_INT, "
				+ "coalesce(coln_int_wc,0) + coalesce(minus_charges_int,0) - coalesce(add_charges_int,0) as COLN_INT, "
				+ "coalesce(dmd_int_for_mth_wc,0) as DMD_INT from public.pms_dcb_trn_bill_dmd "
				+ "where bill_year = :year AND bill_month = :month) A "
				+ "left join public.pms_dcb_mst_beneficiary B on B.beneficiary_sno = A.beneficiary_sno "
				+ "LEFT JOIN public.COM_MST_BLOCKS C ON C.block_sno = B.block_sno "
				+ "LEFT JOIN public.COM_MST_PANCHAYATS D ON D.panch_SNO = B.village_panchayat_sno "
				+ "LEFT JOIN public.COM_MST_DISTRICTS E ON E.DISTRICT_CODE = B.DISTRICT_CODE "
				+ "LEFT JOIN public.region_cir_div_view F ON F.DIDID = A.OFFICE_ID "
				+ "LEFT JOIN public.com_mst_offices OFF ON OFF.office_id = A.OFFICE_ID "
				+ "where B.beneficiary_type_id_sub in (:type1, :type2, :type3, :type4) "
				+ "ORDER BY E.DISTRICT_NAME, B.BENEFICIARY_NAME";

		List<Object[]> queryResults = entityManager.createNativeQuery(sql).setParameter("year", year)
				.setParameter("month", month).setParameter("type1", 1).setParameter("type2", 2).setParameter("type3", 3)
				.setParameter("type4", 4).getResultList();

		List<ReportData> results = new ArrayList<>();
		for (Object[] row : queryResults) {
			ReportData dto = new ReportData();
			dto.setOfficeName((String) row[0]);
			dto.setOfficeId(((BigDecimal) row[1]).longValue());
			dto.setDistrictName((String) row[3]);
			dto.setBeneficiarySno(((BigDecimal) row[4]).longValue());
			dto.setBeneficiaryName((String) row[5]);
			dto.setObWc(((BigDecimal) row[6]).intValue());
			dto.setColnWc(((BigDecimal) row[7]).intValue());
			dto.setBalanceWc(((BigDecimal) row[8]).intValue());
			dto.setDmdWc(((BigDecimal) row[9]).intValue());
			dto.setTotalWc(((BigDecimal) row[10]).intValue());
			dto.setObInt(((BigDecimal) row[11]).intValue());
			dto.setColnInt(((BigDecimal) row[12]).intValue());
			dto.setBalanceInt(((BigDecimal) row[13]).intValue());
			dto.setDmdInt(((BigDecimal) row[14]).intValue());
			dto.setTotalInt(((BigDecimal) row[15]).intValue());
			dto.setTotal(((BigDecimal) row[16]).intValue());
			results.add(dto);
		}

		return results;
	}

	public List<Map<String, Object>> executeReportQuery() {
		String sqlQuery = "SELECT * FROM (SELECT full_view1.office_id, \r\n" + "                off.office_name, \r\n"
				+ "        ben.district_code, \r\n" + "        dst.district_name, \r\n" + "        ben.block_sno, \r\n"
				+ "        bck.block_name, \r\n" + "        ben.village_panchayat_sno as panchayat_code, \r\n"
				+ "        pch.panch_name as panchayat_name, \r\n"
				+ "        full_view1.beneficiary_sno as beneficiary_code, \r\n"
				+ "        ben.lgd_code as lgd_code, \r\n" + "         full_view1.bill_sno, \r\n"
				+ "            full_view1.demand_wc as demand_wc, \r\n"
				+ "            full_view1.demand_int as demand_int, \r\n"
				+ "            full_view1.demand_wc + full_view1.demand_int AS total_dmd \r\n"
				+ "   FROM ( SELECT full_view.bill_sno, \r\n" + "            full_view.office_id, \r\n"
				+ "            full_view.beneficiary_sno, \r\n" + "            full_view.bill_year, \r\n"
				+ "            full_view.bill_month, \r\n"
				+ "            COALESCE(full_view.outstanding_due_wc, 0::numeric) AS outstanding_due_wc, \r\n"
				+ "            COALESCE(full_view.collection_wc, 0::numeric) AS collection_wc, \r\n"
				+ "            COALESCE(full_view.demand_wc, 0::numeric) AS demand_wc, \r\n"
				+ "            COALESCE(full_view.other_charges_wc, 0::numeric) AS other_charges_wc, \r\n"
				+ "            COALESCE(full_view.balance_wc, 0::numeric) AS balance_wc, \r\n"
				+ "            COALESCE(full_view.outstanding_due_int, 0::numeric) AS outstanding_due_int, \r\n"
				+ "            COALESCE(full_view.collection_int, 0::numeric) AS collection_int, \r\n"
				+ "            COALESCE(full_view.demand_int, 0::numeric) AS demand_int, \r\n"
				+ "            COALESCE(full_view.other_charges_int, 0::numeric) AS other_charges_int, \r\n"
				+ "            COALESCE(full_view.balance_int, 0::numeric) AS balance_int \r\n"
				+ "           FROM ( SELECT pms_dcb_trn_bill_dmd.bill_sno, \r\n"
				+ "                    pms_dcb_trn_bill_dmd.office_id, \r\n"
				+ "                    pms_dcb_trn_bill_dmd.beneficiary_sno, \r\n"
				+ "                    pms_dcb_trn_bill_dmd.bill_year, \r\n"
				+ "                    pms_dcb_trn_bill_dmd.bill_month, \r\n"
				+ "                    COALESCE(pms_dcb_trn_bill_dmd.ob_yester_yr_wc, 0::numeric) + COALESCE(pms_dcb_trn_bill_dmd.ob_cur_yr_wc, 0::numeric) AS outstanding_due_wc, \r\n"
				+ "                    COALESCE(pms_dcb_trn_bill_dmd.coln_yester_yr_wc, 0::numeric) + COALESCE(pms_dcb_trn_bill_dmd.coln_cur_yr_wc, 0::numeric) AS collection_wc, \r\n"
				+ "                    pms_dcb_trn_bill_dmd.month_bill_amt AS demand_wc, \r\n"
				+ "                    COALESCE(pms_dcb_trn_bill_dmd.add_charges_wc, 0::numeric) - COALESCE(pms_dcb_trn_bill_dmd.minus_charges_wc, 0::numeric) AS other_charges_wc, \r\n"
				+ "                    COALESCE(pms_dcb_trn_bill_dmd.ob_yester_yr_wc, 0::numeric) + COALESCE(pms_dcb_trn_bill_dmd.ob_cur_yr_wc, 0::numeric) - (COALESCE(pms_dcb_trn_bill_dmd.coln_yester_yr_wc, 0::numeric) + COALESCE(pms_dcb_trn_bill_dmd.coln_cur_yr_wc, 0::numeric)) + COALESCE(pms_dcb_trn_bill_dmd.month_bill_amt, 0::numeric) + (COALESCE(pms_dcb_trn_bill_dmd.add_charges_wc, 0::numeric) - COALESCE(pms_dcb_trn_bill_dmd.minus_charges_wc, 0::numeric)) AS balance_wc, \r\n"
				+ "                    COALESCE(pms_dcb_trn_bill_dmd.ob_int_amt_wc, 0::numeric) AS outstanding_due_int, \r\n"
				+ "                    COALESCE(pms_dcb_trn_bill_dmd.coln_int_wc, 0::numeric) AS collection_int, \r\n"
				+ "                    COALESCE(pms_dcb_trn_bill_dmd.int_calc_wc, 0::numeric) AS demand_int, \r\n"
				+ "                    COALESCE(pms_dcb_trn_bill_dmd.add_charges_int, 0::numeric) - COALESCE(pms_dcb_trn_bill_dmd.minus_charges_int, 0::numeric) AS other_charges_int, \r\n"
				+ "                    COALESCE(pms_dcb_trn_bill_dmd.ob_int_amt_wc, 0::numeric) - COALESCE(pms_dcb_trn_bill_dmd.coln_int_wc, 0::numeric) + (COALESCE(pms_dcb_trn_bill_dmd.int_calc_wc, 0::numeric) + COALESCE(pms_dcb_trn_bill_dmd.add_charges_int, 0::numeric) - COALESCE(pms_dcb_trn_bill_dmd.minus_charges_int, 0::numeric)) AS balance_int \r\n"
				+ "                   FROM pms_dcb_trn_bill_dmd) full_view) \r\n"
				+ "                                     full_view1 \r\n"
				+ "                                     left join pms_dcb_mst_beneficiary ben on ben.beneficiary_sno = full_view1.beneficiary_sno \r\n"
				+ "                                     left join com_mst_districts dst on dst.district_code = ben.district_code \r\n"
				+ "                                     left join com_mst_blocks bck on bck.block_sno = ben.block_sno \r\n"
				+ "                                     left join com_mst_panchayats pch on pch.panch_sno = ben.village_panchayat_sno \r\n"
				+ "                                     left join com_mst_offices off on off.office_id = full_view1.office_id \r\n"
				+ "                                      where bill_year= 2023 and bill_month= 6 and ben.beneficiary_type_id_sub = 6 \r\n"
				+ "                                        and full_view1.beneficiary_sno <> 590 \r\n"
				+ "                                        ) as opt1 \r\n"
				+ "                                        where \r\n"
				+ "                                        opt1.beneficiary_code not in \r\n"
				+ "                                        (select beneficiary_sno from rd_no_dmd_list_may_2024) \r\n"
				+ "                                        OR opt1.beneficiary_code not in  (select beneficiary_sno from full_view_new where bill_year= 2024 and bill_month = 6 and outstanding_due_wc = 0 \r\n"
				+ "                                        and collection_wc = 0 and demand_wc = 0 and other_charges_wc= 0 and balance_wc = 0 \r\n"
				+ "                                        and outstanding_due_int = 0 \r\n"
				+ "                                        and collection_int = 0 and demand_int= 0 and other_charges_int= 0 and balance_int = 0 \r\n"
				+ ") \r\n" + "                                        order by office_name, panchayat_name";

		return jdbcTemplate.queryForList(sqlQuery);
	}

	public List<DCBCollection> generateReport(int year, int month) {
		String sqlQuery = "SELECT Reg.Reg AS Region_Name, " + "Reg.cir AS Circle_name, " + "Reg.div AS Division_name, "
				+ "Arrear_wc, " + "Arrear_int, " + "Arrear, " + "Demand_wc, " + "Demand_int, " + "Demand, "
				+ "Total_Due, " + "Coln_wc, " + "Coln_int, " + "Coln, " + "Balance_due, "
				+ "ROUND((Coln/Demand)*100,2) AS Percentage_of_coln_to_dmd, "
				+ "ROUND((Coln/Total_Due)*100,2) AS Percentage_of_coln_to_Due " + "FROM (" + "   SELECT "
				+ "       Demand.office_id AS Division_code, "
				+ "       COALESCE(Demand.ob_wc,0) - COALESCE(Coln_wc,0) AS Arrear_wc, "
				+ "       COALESCE(Demand.ob_int,0) - COALESCE(Coln_int,0) AS Arrear_int, "
				+ "       (COALESCE(Demand.ob_wc,0) - COALESCE(Coln_wc,0)) + (COALESCE(Demand.ob_int,0) - COALESCE(Coln_int,0)) AS Arrear, "
				+ "       COALESCE(dmd_wc,0) AS Demand_wc, " + "       COALESCE(dmd_int,0) AS Demand_int, "
				+ "       (COALESCE(dmd_wc,0) + COALESCE(dmd_int,0)) AS Demand, "
				+ "       (COALESCE(Demand.ob_wc,0) - COALESCE(Coln_wc,0)) + (COALESCE(Demand.ob_int,0) - COALESCE(Coln_int,0)) "
				+ "           + (COALESCE(dmd_wc,0) + COALESCE(dmd_int,0)) AS Total_Due, "
				+ "       COALESCE(CURRENT_MTH_COLN_WC.amount,0) AS Coln_wc, "
				+ "       COALESCE(CURRENT_MTH_COLN_INT.amount,0) AS Coln_int, "
				+ "       (COALESCE(CURRENT_MTH_COLN_WC.amount,0) + COALESCE(CURRENT_MTH_COLN_INT.amount,0)) AS Coln, "
				+ "       (COALESCE(Demand.ob_wc,0) - COALESCE(Coln_wc,0)) + (COALESCE(Demand.ob_int,0) - COALESCE(Coln_int,0)) "
				+ "           + (COALESCE(dmd_wc,0) + COALESCE(dmd_int,0)) "
				+ "           - (COALESCE(CURRENT_MTH_COLN_WC.amount,0) + COALESCE(CURRENT_MTH_COLN_INT.amount,0)) AS Balance_due "
				+ "   FROM (" + "       SELECT " + "           dmd.office_id, "
				+ "           SUM(COALESCE(dmd.ob_yester_yr_wc,0) + COALESCE(dmd.ob_cur_yr_wc,0) + COALESCE(ob_maint_charges,0)) AS ob_wc, "
				+ "           SUM(COALESCE(dmd.coln_yester_yr_wc,0) + COALESCE(dmd.coln_cur_yr_wc,0) + COALESCE(dmd.coln_maint,0) - COALESCE(add_charges_wc,0) + COALESCE(minus_charges_wc,0) "
				+ "               - COALESCE(add_charges_maint,0) + COALESCE(minus_charges_maint,0)) AS coln_wc, "
				+ "           SUM(COALESCE(wc_mth_total,0)) AS dmd_wc, "
				+ "           SUM(COALESCE(ob_int_amt_wc,0)) AS ob_int, "
				+ "           SUM(COALESCE(coln_int_wc,0) - COALESCE(add_charges_int,0) + COALESCE(minus_charges_int,0)) AS coln_int, "
				+ "           SUM(COALESCE(dmd_int_for_mth_wc)) AS dmd_int " + "       FROM pms_dcb_trn_bill_dmd dmd "
				+ "       WHERE dmd.bill_year = :year AND dmd.bill_month = :month " + "       GROUP BY dmd.office_id "
				+ "   ) AS Demand " + "   LEFT JOIN ( " + "       SELECT " + "           office_id, "
				+ "           SUM(amount) AS amount " + "       FROM ( " + "           SELECT "
				+ "               total.office_id, " + "               ben.beneficiary_type_id_sub, "
				+ "               CASE "
				+ "                   WHEN ben.beneficiary_type_id_sub = 1 THEN ben.added_to_ben_sno "
				+ "                   ELSE ben.beneficiary_sno " + "               END AS beneficiary_sno, "
				+ "               amount " + "           FROM ( " + "               SELECT * "
				+ "               FROM ( " + "                   SELECT " + "                       office_id, "
				+ "                       beneficiary_sno, " + "                       SUM(amount) AS amount "
				+ "                   FROM ( " + "                       SELECT "
				+ "                           pms_dcb_fas_receipt_view.receipt_no, "
				+ "                           pms_dcb_fas_receipt_view.accounting_for_office_id AS office_id, "
				+ "                           pms_dcb_fas_receipt_view.cashbook_year AS YEAR, "
				+ "                           pms_dcb_fas_receipt_view.cashbook_month AS MONTH, "
				+ "                           pms_dcb_fas_receipt_view.sub_ledger_type_code, "
				+ "                           pms_dcb_fas_receipt_view.sub_ledger_code, "
				+ "                           pms_dcb_fas_receipt_view.sch_type_id, "
				+ "                           pms_dcb_fas_receipt_view.sch_sno, "
				+ "                           pms_dcb_fas_receipt_view.beneficiary_name, "
				+ "                           pms_dcb_fas_receipt_view.beneficiary_sno, "
				+ "                           pms_dcb_fas_receipt_view.beneficiary_type_id_sub, "
				+ "                           pms_dcb_fas_receipt_view.amount "
				+ "                       FROM pms_dcb_fas_receipt_view "
				+ "                       WHERE (pms_dcb_fas_receipt_view.account_head_code = ANY (ARRAY [782401, 782402, 782403, 782404, 782405, 782406, 782407, 900108, 900109])) "
				+ "                           AND cashbook_year = :year AND cashbook_month = :month "
				+ "                   ) opt1 " + "                   GROUP BY office_id, beneficiary_sno "
				+ "               ) receipt " + "               UNION ALL " + "               SELECT * "
				+ "               FROM ( " + "                   SELECT "
				+ "                       cast(opt4.office_id as numeric) AS office_id, "
				+ "                       opt4.beneficiary_sno, " + "                       SUM(opt4.amount) AS amount "
				+ "                   FROM ( " + "                       SELECT "
				+ "                           pms_dcb_other_charges.office_id, "
				+ "                           pms_dcb_other_charges.beneficiary_sno, "
				+ "                           pms_dcb_other_charges.cashbook_year AS YEAR, "
				+ "                           pms_dcb_other_charges.cashbook_month AS MONTH, "
				+ "                           SUM(COALESCE(CASE WHEN pms_dcb_other_charges.cr_dr_indicator = 'CR' THEN pms_dcb_other_charges.amount ELSE NULL END, 0)) AS amount "
				+ "                       FROM pms_dcb_other_charges "
				+ "                       WHERE (pms_dcb_other_charges.account_head_code IN (SELECT DISTINCT pms_dcb_receipt_account_map.account_head_code FROM pms_dcb_receipt_account_map WHERE pms_dcb_receipt_account_map.collection_type = 7)) "
				+ "                       GROUP BY pms_dcb_other_charges.office_id, pms_dcb_other_charges.beneficiary_sno, pms_dcb_other_charges.cashbook_year, pms_dcb_other_charges.cashbook_month "
				+ "                       ORDER BY pms_dcb_other_charges.office_id, pms_dcb_other_charges.beneficiary_sno "
				+ "                   ) AS opt4 "
				+ "                   WHERE opt4.YEAR = :year AND opt4.MONTH = :month "
				+ "                   GROUP BY opt4.office_id, opt4.beneficiary_sno " + "               ) adj "
				+ "           ) total "
				+ "           LEFT JOIN pms_dcb_mst_beneficiary ben ON ben.beneficiary_sno = total.beneficiary_sno "
				+ "       ) AS opt1 " + "       GROUP BY office_id "
				+ "   ) CURRENT_MTH_COLN_WC ON CURRENT_MTH_COLN_WC.office_id = Demand.office_id " + "   LEFT JOIN ( "
				+ "       SELECT " + "           office_id, " + "           SUM(amount) AS amount " + "       FROM ( "
				+ "           SELECT " + "               total.office_id, "
				+ "               ben.beneficiary_type_id_sub, " + "               CASE "
				+ "                   WHEN ben.beneficiary_type_id_sub = 1 THEN ben.added_to_ben_sno "
				+ "                   ELSE ben.beneficiary_sno " + "               END AS beneficiary_sno, "
				+ "               amount " + "           FROM ( " + "               SELECT * "
				+ "               FROM ( " + "                   SELECT " + "                       office_id, "
				+ "                       beneficiary_sno, " + "                       SUM(amount) AS amount "
				+ "                   FROM ( " + "                       SELECT "
				+ "                           pms_dcb_fas_receipt_view.receipt_no, "
				+ "                           pms_dcb_fas_receipt_view.accounting_for_office_id AS office_id, "
				+ "                           pms_dcb_fas_receipt_view.cashbook_year AS YEAR, "
				+ "                           pms_dcb_fas_receipt_view.cashbook_month AS MONTH, "
				+ "                           pms_dcb_fas_receipt_view.sub_ledger_type_code, "
				+ "                           pms_dcb_fas_receipt_view.sub_ledger_code, "
				+ "                           pms_dcb_fas_receipt_view.sch_type_id, "
				+ "                           pms_dcb_fas_receipt_view.sch_sno, "
				+ "                           pms_dcb_fas_receipt_view.beneficiary_name, "
				+ "                           pms_dcb_fas_receipt_view.beneficiary_sno, "
				+ "                           pms_dcb_fas_receipt_view.beneficiary_type_id_sub, "
				+ "                           pms_dcb_fas_receipt_view.amount "
				+ "                       FROM pms_dcb_fas_receipt_view "
				+ "                       WHERE (pms_dcb_fas_receipt_view.account_head_code = ANY (ARRAY [120601])) "
				+ "                           AND cashbook_year = :year AND cashbook_month = :month "
				+ "                   ) opt1 " + "                   GROUP BY office_id, beneficiary_sno "
				+ "               ) receipt " + "               UNION ALL " + "               SELECT * "
				+ "               FROM ( " + "                   SELECT "
				+ "                       cast(opt4.office_id as numeric) AS office_id, "
				+ "                       opt4.beneficiary_sno, " + "                       SUM(opt4.amount) AS amount "
				+ "                   FROM ( " + "                       SELECT "
				+ "                           pms_dcb_other_charges.office_id, "
				+ "                           pms_dcb_other_charges.beneficiary_sno, "
				+ "                           pms_dcb_other_charges.cashbook_year AS YEAR, "
				+ "                           pms_dcb_other_charges.cashbook_month AS MONTH, "
				+ "                           SUM(COALESCE(CASE WHEN pms_dcb_other_charges.cr_dr_indicator= 'CR' THEN pms_dcb_other_charges.amount ELSE NULL END, 0)) AS amount "
				+ "                       FROM pms_dcb_other_charges "
				+ "                       WHERE (pms_dcb_other_charges.account_head_code IN (SELECT DISTINCT pms_dcb_receipt_account_map.account_head_code FROM pms_dcb_receipt_account_map WHERE pms_dcb_receipt_account_map.collection_type = 9)) "
				+ "                       GROUP BY pms_dcb_other_charges.office_id, pms_dcb_other_charges.beneficiary_sno, pms_dcb_other_charges.cashbook_year, pms_dcb_other_charges.cashbook_month "
				+ "                       ORDER BY pms_dcb_other_charges.office_id, pms_dcb_other_charges.beneficiary_sno "
				+ "                   ) AS opt4 "
				+ "                   WHERE opt4.YEAR = :year AND opt4.MONTH = :month "
				+ "                   GROUP BY opt4.office_id, opt4.beneficiary_sno " + "               ) adj "
				+ "           ) total "
				+ "           LEFT JOIN pms_dcb_mst_beneficiary ben ON ben.beneficiary_sno = total.beneficiary_sno "
				+ "       ) AS opt1 " + "       GROUP BY office_id "
				+ "   ) CURRENT_MTH_COLN_INT ON CURRENT_MTH_COLN_INT.office_id = Demand.office_id " + ") AS Demand_Grp "
				+ "LEFT JOIN region_cir_div_view Reg ON Reg.didid = Demand_Grp.Division_code "
				+ "ORDER BY Reg.Reg, Reg.cir, Reg.div";

		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("year", year);
		query.setParameter("month", month);
		List<Object[]> results = query.getResultList();

		List<DCBCollection> dcbCollections = new ArrayList<>();
		for (Object[] result : results) {
			DCBCollection collection = new DCBCollection();
			collection.setRegionName((String) result[0]);
			collection.setCircleName((String) result[1]);
			collection.setDivisionName((String) result[2]);
			collection.setArrearWc(((BigDecimal) result[3]).intValue());
			collection.setArrearInt(((BigDecimal) result[4]).intValue());
			collection.setArrear(((BigDecimal) result[5]).intValue());
			collection.setDemandWc(((BigDecimal) result[6]).intValue());
			collection.setDemandInt(((BigDecimal) result[7]).intValue());
			collection.setDemand(((BigDecimal) result[8]).intValue());
			collection.setTotalDue(((BigDecimal) result[9]).intValue());
			collection.setColnWc(((BigDecimal) result[10]).intValue());
			collection.setColnInt(((BigDecimal) result[11]).intValue());
			collection.setColn(((BigDecimal) result[12]).intValue());
			collection.setBalanceDue(((BigDecimal) result[13]).intValue());
			collection.setPercentageOfColnToDemand(((BigDecimal) result[14]).intValue());
			collection.setPercentageOfColnToDue(((BigDecimal) result[15]).intValue());
			dcbCollections.add(collection);
		}

		return dcbCollections;
	}

	public List<Map<String, Object>> executeReportQuery2(int year, int month) {
		String sql = "SELECT " + "Reg.reg as region_name, " + "Reg.cir as circle_name, " + "Dmd.office_id, "
				+ "Reg.div as division_name, "
				+ "Count(Case when Dmd.beneficiary_type_id_sub = 1 Then 1 End) as Corporation, "
				+ "Count(Case when Dmd.beneficiary_type_id_sub in (2,3) Then 1 End) as Municipality, "
				+ "Count(Case when Dmd.beneficiary_type_id_sub = 4 Then 1 End) as Town_Panchayat, "
				+ "Count(Case when Dmd.beneficiary_type_id_sub = 6 Then 1 End) as Village_Panchayat, "
				+ "Count(Case when Dmd.beneficiary_type_id_sub not in (1,2,3,4,6) Then 1 End) as Others, "
				+ "Count(*) as Total, "
				+ "Count(Case when (Dmd.beneficiary_type_id_sub = 1 and ((arrear_wc + arrear_int)/ (dmd_wc)) > 24) Then 1 End) as Corp_greater_than_24, "
				+ "Count(Case when (Dmd.beneficiary_type_id_sub in (2,3) and ((arrear_wc + arrear_int)/ (dmd_wc)) > 24) Then 1 End) as Mun_greater_than_24, "
				+ "Count(Case when (Dmd.beneficiary_type_id_sub = 4 and ((arrear_wc + arrear_int)/ (dmd_wc)) > 24) Then 1 End) as Tp_greater_than_24, "
				+ "Count(Case when (Dmd.beneficiary_type_id_sub = 6 and ((arrear_wc + arrear_int)/ (dmd_wc)) > 24) Then 1 End) as Vp_greater_than_24, "
				+ "Count(Case when (Dmd.beneficiary_type_id_sub not in (1,2,3,4,6) and ((arrear_wc + arrear_int)/ (dmd_wc)) > 24) Then 1 End) as Others_greater_than_24, "
				+ "Count(Case when (((arrear_wc + arrear_int)/ (dmd_wc)) > 24) Then 1 End) as Total_greater_than_24, "
				+ "Count(Case when (Dmd.beneficiary_type_id_sub = 1 and ((arrear_wc + arrear_int)/ (dmd_wc)) > 12 AND ((arrear_wc + arrear_int)/ (dmd_wc)) < 24 ) Then 1 End) as Corp_greater_than_12, "
				+ "Count(Case when (Dmd.beneficiary_type_id_sub in (2,3) and ((arrear_wc + arrear_int)/ (dmd_wc)) > 12 AND ((arrear_wc + arrear_int)/ (dmd_wc)) < 24 ) Then 1 End) as Mun_greater_than_12, "
				+ "Count(Case when (Dmd.beneficiary_type_id_sub = 4 and ((arrear_wc + arrear_int)/ (dmd_wc)) > 12 AND ((arrear_wc + arrear_int)/ (dmd_wc)) < 24 ) Then 1 End) as Tp_greater_than_12, "
				+ "Count(Case when (Dmd.beneficiary_type_id_sub = 6 and ((arrear_wc + arrear_int)/ (dmd_wc)) > 12 AND ((arrear_wc + arrear_int)/ (dmd_wc)) < 24 ) Then 1 End) as Vp_greater_than_12, "
				+ "Count(Case when (Dmd.beneficiary_type_id_sub not in (1,2,3,4,6) and ((arrear_wc + arrear_int)/ (dmd_wc)) > 12 AND ((arrear_wc + arrear_int)/ (dmd_wc)) < 24 ) Then 1 End) as Others_greater_than_12, "
				+ "Count(Case when (((arrear_wc + arrear_int)/ (dmd_wc)) > 12 AND (((arrear_wc + arrear_int)/ (dmd_wc)) < 24 )) Then 1 End ) as Total_greater_than_12 "
				+ "FROM ( " + "    SELECT office_id, beneficiary_sno, beneficiary_type_id_sub, "
				+ "    SUM(dmd_wc) AS dmd_wc, " + "    SUM(arrear_wc) AS arrear_wc, " + "    SUM(dmd_int) AS dmd_int, "
				+ "    SUM(arrear_int) AS arrear_int " + "    FROM ( " + "        SELECT dmd.office_id, "
				+ "        CASE "
				+ "        WHEN ben.beneficiary_type_id_sub = 1 THEN ben.added_to_ben_sno ELSE dmd.beneficiary_sno "
				+ "        END AS beneficiary_sno, " + "        ben.beneficiary_type_id_sub, "
				+ "        (wc_mth_total) AS dmd_wc, "
				+ "        (COALESCE(ob_yester_yr_wc, 0) + COALESCE(ob_cur_yr_wc, 0) + COALESCE(ob_maint_charges, 0)) - "
				+ "        (COALESCE(dmd.coln_yester_yr_wc, 0) + COALESCE(dmd.coln_cur_yr_wc, 0) + COALESCE(dmd.coln_maint, 0) - "
				+ "        COALESCE(add_charges_wc, 0) + COALESCE(minus_charges_wc, 0) - COALESCE(add_charges_maint, 0) + COALESCE(minus_charges_maint, 0)) AS Arrear_wc, "
				+ "        (dmd_int_for_mth_wc) AS dmd_int, "
				+ "        (COALESCE(ob_int_amt_wc, 0)) - (COALESCE(coln_int_wc, 0) - COALESCE(add_charges_int, 0) + COALESCE(minus_charges_int, 0)) AS Arrear_int "
				+ "        FROM pms_dcb_trn_bill_dmd dmd "
				+ "        LEFT JOIN pms_dcb_mst_beneficiary ben ON ben.beneficiary_sno = dmd.beneficiary_sno "
				+ "        WHERE dmd.bill_year = ? AND dmd.bill_month = ? " + "    ) AS Demand "
				+ "    GROUP BY office_id, beneficiary_sno, beneficiary_type_id_sub " + ") AS Dmd "
				+ "LEFT JOIN region_cir_div_view Reg ON Reg.didid = Dmd.office_id "
				+ "WHERE Dmd.dmd_wc > 0 AND beneficiary_type_id_sub > 6 "
				+ "GROUP BY Dmd.office_id, Reg.reg, Reg.cir, Reg.div " + "ORDER BY Reg.reg, Reg.cir, Reg.div";

		return jdbcTemplate.queryForList(sql, year, month);
	}

	@Override
	public List<Map<String, Object>> executeReportQuery3(int year, int month) {
		String sql = "SELECT reg.reg AS region_name, reg.cir AS circle_name, reg.div AS division_name,\r\n"
				+ "       COUNT(CASE WHEN beneficiary_type_id_sub = 1 THEN 1 END) AS Corporation_count,\r\n"
				+ "       SUM(CASE WHEN beneficiary_type_id_sub = 1 THEN balance ELSE 0 END) AS Corporation_balance,\r\n"
				+ "       COUNT(CASE WHEN beneficiary_type_id_sub IN (2, 3) THEN 1 END) AS Municipality_count,\r\n"
				+ "       SUM(CASE WHEN beneficiary_type_id_sub IN (2, 3) THEN balance ELSE 0 END) AS Municipality_balance,\r\n"
				+ "       COUNT(CASE WHEN beneficiary_type_id_sub = 4 THEN 1 END) AS TP_count,\r\n"
				+ "       SUM(CASE WHEN beneficiary_type_id_sub = 4 THEN balance ELSE 0 END) AS TP_balance,\r\n"
				+ "       COUNT(CASE WHEN beneficiary_type_id_sub = 6 THEN 1 END) AS VP_count,\r\n"
				+ "       SUM(CASE WHEN beneficiary_type_id_sub = 6 THEN balance ELSE 0 END) AS VP_balance,\r\n"
				+ "       COUNT(CASE WHEN beneficiary_type_id_sub NOT IN (1, 2, 3, 4, 6) THEN 1 END) AS Others_count,\r\n"
				+ "       SUM(CASE WHEN beneficiary_type_id_sub NOT IN (1, 2, 3, 4, 6) THEN balance ELSE 0 END) AS Others_balance\r\n"
				+ "FROM (\r\n"
				+ "    SELECT Demand.office_id, Demand.beneficiary_sno, Demand.beneficiary_type_id_sub, Demand.dmd_wc, Demand.arrear_wc,\r\n"
				+ "           COALESCE(FAS_COLN_WC.amount, 0) AS Coln_wc, Demand.dmd_int, Demand.arrear_int,\r\n"
				+ "           COALESCE(FAS_COLN_INT.amount, 0) AS Coln_int,\r\n"
				+ "           (COALESCE(Demand.arrear_wc, 0) + COALESCE(Demand.dmd_wc, 0) - COALESCE(FAS_COLN_WC.amount, 0)) AS Balance_wc,\r\n"
				+ "           (COALESCE(Demand.arrear_int, 0) + COALESCE(Demand.dmd_int, 0) - COALESCE(FAS_COLN_INT.amount, 0)) AS Balance_int,\r\n"
				+ "           (COALESCE(Demand.arrear_wc, 0) + COALESCE(Demand.dmd_wc, 0) - COALESCE(FAS_COLN_WC.amount, 0)) +\r\n"
				+ "           (COALESCE(Demand.arrear_int, 0) + COALESCE(Demand.dmd_int, 0) - COALESCE(FAS_COLN_INT.amount, 0)) AS Balance\r\n"
				+ "    FROM (\r\n"
				+ "        SELECT office_id, beneficiary_sno, beneficiary_type_id_sub,\r\n"
				+ "               SUM(dmd_wc) AS dmd_wc, SUM(arrear_wc) AS arrear_wc, SUM(dmd_int) AS dmd_int, SUM(arrear_int) AS arrear_int\r\n"
				+ "        FROM (\r\n"
				+ "            SELECT dmd.office_id,\r\n"
				+ "                   CASE WHEN ben.beneficiary_type_id_sub = 1 THEN ben.added_to_ben_sno ELSE dmd.beneficiary_sno END AS beneficiary_sno,\r\n"
				+ "                   ben.beneficiary_type_id_sub,\r\n"
				+ "                   (wc_mth_total) AS dmd_wc,\r\n"
				+ "                   (COALESCE(ob_yester_yr_wc, 0) + COALESCE(ob_cur_yr_wc, 0) + COALESCE(ob_maint_charges, 0)) -\r\n"
				+ "                   (COALESCE(dmd.coln_yester_yr_wc, 0) + COALESCE(dmd.coln_cur_yr_wc, 0) + COALESCE(dmd.coln_maint, 0) -\r\n"
				+ "                    COALESCE(add_charges_wc, 0) + COALESCE(minus_charges_wc, 0) - COALESCE(add_charges_maint, 0) + COALESCE(minus_charges_maint, 0)) AS Arrear_wc,\r\n"
				+ "                   (dmd_int_for_mth_wc) AS dmd_int,\r\n"
				+ "                   (COALESCE(ob_int_amt_wc, 0)) - (COALESCE(coln_int_wc, 0) - COALESCE(add_charges_int, 0) + COALESCE(minus_charges_int, 0)) AS Arrear_int\r\n"
				+ "            FROM pms_dcb_trn_bill_dmd dmd\r\n"
				+ "            LEFT JOIN pms_dcb_mst_beneficiary ben ON ben.beneficiary_sno = dmd.beneficiary_sno\r\n"
				+ "            WHERE dmd.bill_year = ? AND dmd.bill_month = ?\r\n"
				+ "        ) AS Demand\r\n"
				+ "        GROUP BY office_id, beneficiary_sno, beneficiary_type_id_sub\r\n"
				+ "    ) AS Demand\r\n"
				+ "    LEFT JOIN (\r\n"
				+ "        SELECT beneficiary_sno, beneficiary_type_id_sub, office_id, SUM(amount) AS amount\r\n"
				+ "        FROM (\r\n"
				+ "            SELECT receipt.office_id, ben.beneficiary_type_id_sub,\r\n"
				+ "                   CASE WHEN ben.beneficiary_type_id_sub = 1 THEN ben.added_to_ben_sno ELSE ben.beneficiary_sno END AS beneficiary_sno,\r\n"
				+ "                   amount\r\n"
				+ "            FROM (\r\n"
				+ "                SELECT office_id, beneficiary_sno, SUM(amount) AS amount\r\n"
				+ "                FROM (\r\n"
				+ "                    SELECT pms_dcb_fas_receipt_view.receipt_no, pms_dcb_fas_receipt_view.accounting_for_office_id AS office_id,\r\n"
				+ "                           pms_dcb_fas_receipt_view.cashbook_year AS YEAR, pms_dcb_fas_receipt_view.cashbook_month AS MONTH,\r\n"
				+ "                           pms_dcb_fas_receipt_view.sub_ledger_type_code, pms_dcb_fas_receipt_view.sub_ledger_code,\r\n"
				+ "                           pms_dcb_fas_receipt_view.sch_type_id, pms_dcb_fas_receipt_view.sch_sno, pms_dcb_fas_receipt_view.beneficiary_name,\r\n"
				+ "                           pms_dcb_fas_receipt_view.beneficiary_sno, pms_dcb_fas_receipt_view.beneficiary_type_id_sub, pms_dcb_fas_receipt_view.amount\r\n"
				+ "                    FROM pms_dcb_fas_receipt_view\r\n"
				+ "                    WHERE (pms_dcb_fas_receipt_view.account_head_code = ANY (ARRAY [782401, 782402, 782403, 782404, 782405, 782406, 782407, 900108, 900109, 480420]))\r\n"
				+ "                    AND cashbook_year = ? AND cashbook_month = ?\r\n"
				+ "                ) opt1\r\n"
				+ "                GROUP BY office_id, beneficiary_sno\r\n"
				+ "            ) receipt\r\n"
				+ "            LEFT JOIN pms_dcb_mst_beneficiary ben ON ben.beneficiary_sno = receipt.beneficiary_sno\r\n"
				+ "            UNION ALL\r\n"
				+ "            SELECT opt4.office_id, opt4.beneficiary_sno, SUM(opt4.amount) AS amount\r\n"
				+ "            FROM (\r\n"
				+ "                SELECT pms_dcb_other_charges.office_id, pms_dcb_other_charges.beneficiary_sno,\r\n"
				+ "                       pms_dcb_other_charges.cashbook_year AS YEAR, pms_dcb_other_charges.cashbook_month AS MONTH,\r\n"
				+ "                       SUM(COALESCE(CASE WHEN pms_dcb_other_charges.cr_dr_indicator = 'CR' THEN pms_dcb_other_charges.amount ELSE NULL END, 0)) AS amount\r\n"
				+ "                FROM pms_dcb_other_charges\r\n"
				+ "                WHERE pms_dcb_other_charges.account_head_code IN (SELECT DISTINCT pms_dcb_receipt_account_map.account_head_code FROM pms_dcb_receipt_account_map WHERE pms_dcb_receipt_account_map.collection_type = 7)\r\n"
				+ "                GROUP BY pms_dcb_other_charges.office_id, pms_dcb_other_charges.beneficiary_sno, pms_dcb_other_charges.cashbook_year, pms_dcb_other_charges.cashbook_month\r\n"
				+ "            ) AS opt4\r\n"
				+ "            WHERE opt4.YEAR = ? AND opt4.MONTH = ?\r\n"
				+ "            GROUP BY opt4.office_id, opt4.beneficiary_sno\r\n"
				+ "        ) adj\r\n"
				+ "        GROUP BY beneficiary_sno, beneficiary_type_id_sub, office_id\r\n"
				+ "    ) FAS_COLN_WC ON FAS_COLN_WC.beneficiary_sno = Demand.beneficiary_sno AND FAS_COLN_WC.office_id = Demand.office_id\r\n"
				+ "    LEFT JOIN (\r\n"
				+ "        SELECT beneficiary_sno, beneficiary_type_id_sub, office_id, SUM(amount) AS amount\r\n"
				+ "        FROM (\r\n"
				+ "            SELECT receipt.office_id, ben.beneficiary_type_id_sub,\r\n"
				+ "                   CASE WHEN ben.beneficiary_type_id_sub = 1 THEN ben.added_to_ben_sno ELSE ben.beneficiary_sno END AS beneficiary_sno,\r\n"
				+ "                   amount\r\n"
				+ "            FROM (\r\n"
				+ "                SELECT office_id, beneficiary_sno, SUM(amount) AS amount\r\n"
				+ "                FROM (\r\n"
				+ "                    SELECT pms_dcb_fas_receipt_view.receipt_no, pms_dcb_fas_receipt_view.accounting_for_office_id AS office_id,\r\n"
				+ "                           pms_dcb_fas_receipt_view.cashbook_year AS YEAR, pms_dcb_fas_receipt_view.cashbook_month AS MONTH,\r\n"
				+ "                           pms_dcb_fas_receipt_view.sub_ledger_type_code, pms_dcb_fas_receipt_view.sub_ledger_code,\r\n"
				+ "                           pms_dcb_fas_receipt_view.sch_type_id, pms_dcb_fas_receipt_view.sch_sno, pms_dcb_fas_receipt_view.beneficiary_name,\r\n"
				+ "                           pms_dcb_fas_receipt_view.beneficiary_sno, pms_dcb_fas_receipt_view.beneficiary_type_id_sub, pms_dcb_fas_receipt_view.amount\r\n"
				+ "                    FROM pms_dcb_fas_receipt_view\r\n"
				+ "                    WHERE (pms_dcb_fas_receipt_view.account_head_code = ANY (ARRAY [782401, 782402, 782403, 782404, 782405, 782406, 782407, 900108, 900109, 480420]))\r\n"
				+ "                    AND cashbook_year = ? AND cashbook_month = ?\r\n"
				+ "                ) opt1\r\n"
				+ "                GROUP BY office_id, beneficiary_sno\r\n"
				+ "            ) receipt\r\n"
				+ "            LEFT JOIN pms_dcb_mst_beneficiary ben ON ben.beneficiary_sno = receipt.beneficiary_sno\r\n"
				+ "            UNION ALL\r\n"
				+ "            SELECT opt4.office_id, opt4.beneficiary_sno, SUM(opt4.amount) AS amount\r\n"
				+ "            FROM (\r\n"
				+ "                SELECT pms_dcb_other_charges.office_id, pms_dcb_other_charges.beneficiary_sno,\r\n"
				+ "                       pms_dcb_other_charges.cashbook_year AS YEAR, pms_dcb_other_charges.cashbook_month AS MONTH,\r\n"
				+ "                       SUM(COALESCE(CASE WHEN pms_dcb_other_charges.cr_dr_indicator = 'CR' THEN pms_dcb_other_charges.amount ELSE NULL END, 0)) AS amount\r\n"
				+ "                FROM pms_dcb_other_charges\r\n"
				+ "                WHERE pms_dcb_other_charges.account_head_code IN (SELECT DISTINCT pms_dcb_receipt_account_map.account_head_code FROM pms_dcb_receipt_account_map WHERE pms_dcb_receipt_account_map.collection_type = 7)\r\n"
				+ "                GROUP BY pms_dcb_other_charges.office_id, pms_dcb_other_charges.beneficiary_sno, pms_dcb_other_charges.cashbook_year, pms_dcb_other_charges.cashbook_month\r\n"
				+ "            ) AS opt4\r\n"
				+ "            WHERE opt4.YEAR = ? AND opt4.MONTH = ?\r\n"
				+ "            GROUP BY opt4.office_id, opt4.beneficiary_sno\r\n"
				+ "        ) adj\r\n"
				+ "        GROUP BY beneficiary_sno, beneficiary_type_id_sub, office_id\r\n"
				+ "    ) FAS_COLN_INT ON FAS_COLN_INT.beneficiary_sno = Demand.beneficiary_sno AND FAS_COLN_INT.office_id = Demand.office_id\r\n"
				+ "    GROUP BY reg.reg, reg.cir, reg.div";

		return jdbcTemplate.query(sql,
				new Object[] { year, month, year, month, year, month, year, month, year, month },
				new ColumnMapRowMapper());
	}

	@Override
	public List<Map<String, Object>> getAadhaarStatusReport(int paymentOfficeId) {
		
		return pensionerEntityRepo.getAadhaarStatusReport(paymentOfficeId);
	}

	@Override
	public List<Map<String, Object>> getAadhaarStatusDetailed(int paymentOfficeId, String pensionerTypeId, String status) {
		return pensionerEntityRepo.getAadhaarStatusDetailed(paymentOfficeId, pensionerTypeId, status);
	}

	@Override
	public List<Map<String, Object>> getAadhaarStatusReportAllOffices() {
		// TODO Auto-generated method stub
		return pensionerEntityRepo.getAadhaarStatusReportAllOffices();
	}



}
