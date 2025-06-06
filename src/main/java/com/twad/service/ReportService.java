package com.twad.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.twad.entity.DCBCollection;
import com.twad.entity.ReportData;




@Service
public interface ReportService {

	public List<ReportData> generateMonthEndBalanceReport(int year, int month);

	public List<DCBCollection> generateReport(int year, int month);

	public List<Map<String, Object>> executeReportQuery();

	public List<Map<String, Object>> executeReportQuery2(int year, int month);

	public List<Map<String, Object>> executeReportQuery3(int year , int month);
	
}
