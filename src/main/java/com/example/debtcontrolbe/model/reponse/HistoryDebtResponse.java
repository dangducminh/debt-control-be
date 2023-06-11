package com.example.debtcontrolbe.model.reponse;


import com.example.debtcontrolbe.model.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDebtResponse {
    private Long id;
    private String companyName;
    private Double totalMoney;
    private Double amountPaid;
    private Double amountOwed;
    private LocalDateTime date;
}
