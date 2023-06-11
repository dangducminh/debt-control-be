package com.example.debtcontrolbe.repository;

import com.example.debtcontrolbe.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt,Long> {

    @Query(
            value = "SELECT * \n" +
                    "FROM debt d  \n" +
                    "WHERE d.company_id = :companyId \n" +
                    "order by date DESC\n" +
                    "limit 12",
            nativeQuery = true)
    List<Debt> findTheLatest12RecordByCompanyCodeOrderByDate(Long companyId);


    @Query(
            value = "SELECT * \n" +
                    "FROM debt d  \n" +
                    "WHERE d.company_id = :companyId \n" +
                    "order by date DESC\n" +
                    "limit 1",
            nativeQuery = true)
    Debt getLatestDebt(Long companyId);
}
