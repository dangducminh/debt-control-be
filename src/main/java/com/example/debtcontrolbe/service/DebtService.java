package com.example.debtcontrolbe.service;

import com.example.debtcontrolbe.model.Debt;
import com.example.debtcontrolbe.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtService {

    @Autowired
    private DebtRepository debtRepository;

    public List<Debt> getDebtHistory(String companyCode){
        return debtRepository.findLast12ByCompany(companyCode);
    }

}
