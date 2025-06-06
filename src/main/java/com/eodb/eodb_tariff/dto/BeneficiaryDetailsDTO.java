package com.eodb.eodb_tariff.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeneficiaryDetailsDTO {
    private Long beneficiarySno;
    private String beneficiaryName;
    private Long billSno;
    private Date billingDate;
    private Date latestReceiptDate;
    private String freezedFlag;
    private BigDecimal due;
    private BigDecimal dueAsOnDate;
    private Date nextBillDue;
    private String applicationNo;

    // Getters and setters
}
