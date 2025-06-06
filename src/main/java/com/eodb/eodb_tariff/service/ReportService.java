package com.eodb.eodb_tariff.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eodb.eodb_tariff.entity.DCBCollection;
import com.eodb.eodb_tariff.entity.ReportData;




@Service
public interface ReportService {

	public List<ReportData> generateMonthEndBalanceReport(int year, int month);

	public List<DCBCollection> generateReport(int year, int month);

	public List<Map<String, Object>> executeReportQuery();

	public List<Map<String, Object>> executeReportQuery2(int year, int month);

	public List<Map<String, Object>> executeReportQuery3(int year , int month);
	
}
