package com.example.debtcontrolbe.repository;

import com.example.debtcontrolbe.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    List<Company> findByCompanyCodeContains(String companyCode);

    Company findAllByCompanyCode(String companyCode);
}
