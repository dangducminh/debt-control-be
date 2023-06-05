package com.example.debtcontrolbe.repository;

import com.example.debtcontrolbe.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt,Long> {
    List<Debt> findLast12ByCompany(String companyCode);
}
