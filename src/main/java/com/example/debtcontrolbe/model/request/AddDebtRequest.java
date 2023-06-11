package com.example.debtcontrolbe.model.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddDebtRequest {

    private String companyCode;

    private Double totalMoney;

    private Double amountPaid;

    private Double amountOwed;
}
