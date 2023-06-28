package com.example.debtcontrolbe.repository;

import com.example.debtcontrolbe.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    List<Company> findByCompanyCodeContains(String companyCode);

    Optional<Company> findByCompanyCode(String companyCode);

    @Modifying
    @Transactional
    @Query(
            value = "DELETE " +
                    "FROM company \n" +
                    "WHERE company_code = :companyCode \n",
            nativeQuery = true)
    void deleteByCompanyCode(String companyCode);
}
