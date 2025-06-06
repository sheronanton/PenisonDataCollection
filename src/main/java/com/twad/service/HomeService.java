package com.twad.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twad.dto.BeneficiaryDetailsDTO;
import com.twad.repo.HomeRepository;

@Service
public class HomeService {

    @Autowired
    private HomeRepository homeRepository;

    public List<BeneficiaryDetailsDTO> getSrnEodbMonthlyTariff(int[] beneficiaries, Date receiptDate) {
        // Call the repository method to execute PostgreSQL function
        List<Map<String, Object>> resultList = homeRepository.callSrnEodbMonthlyTariff(beneficiaries, receiptDate);

         resultList = homeRepository.getMonthlyTariff();

        
        
        // Transform Map results into BeneficiaryDetailsDTO objects
        List<BeneficiaryDetailsDTO> beneficiaryDetailsList = new ArrayList<>();
        for (Map<String, Object> result : resultList) {
            BeneficiaryDetailsDTO dto = new BeneficiaryDetailsDTO();
            dto.setBeneficiarySno((Long) result.get("beneficiary_sno"));
            dto.setBeneficiaryName((String) result.get("beneficiary_name"));
            dto.setBillSno((Long) result.get("bill_sno"));
            dto.setBillingDate((Date) result.get("billing_date"));
            dto.setLatestReceiptDate( (Date) result.get("latest_receipt_date"));
            dto.setFreezedFlag((String) result.get("freezed_flag"));
//            dto.setDue(result.get("due") != null ?  result.get("due") : 0);
            dto.setDue((BigDecimal)result.get("due") );
            dto.setDueAsOnDate((BigDecimal)result.get("due_as_on_date"));
            
            dto.setNextBillDue((Date) result.get("next_bill_due"));
            dto.setApplicationNo((String) result.get("application_no"));

            beneficiaryDetailsList.add(dto);
        }

        return beneficiaryDetailsList;
    }
}
