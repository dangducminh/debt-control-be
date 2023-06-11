package com.example.debtcontrolbe.repository;

import com.example.debtcontrolbe.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    List<Company> findByCompanyCodeContains(String companyCode);

    Optional<Company> findByCompanyCode(String companyCode);
}
