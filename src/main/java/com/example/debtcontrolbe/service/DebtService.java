package com.example.debtcontrolbe.service;

import com.example.debtcontrolbe.model.Company;
import com.example.debtcontrolbe.model.Debt;
import com.example.debtcontrolbe.model.reponse.HistoryDebtResponse;
import com.example.debtcontrolbe.model.request.AddDebtRequest;
import com.example.debtcontrolbe.repository.CompanyRepository;
import com.example.debtcontrolbe.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DebtService {

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public List<HistoryDebtResponse> getDebtHistory(String companyCode){
        Company company = companyRepository.findByCompanyCode(companyCode).get();
        List<HistoryDebtResponse> debts = debtRepository.findTheLatest12RecordByCompanyCodeOrderByDate(company.getId())
                .stream()
                .map(i -> new HistoryDebtResponse(i.getId(),i.getCompany().getNameCompany(),i.getTotalMoney(),i.getAmountPaid(),i.getAmountOwed(),i.getDate()))
                .collect(Collectors.toList());
        return debts;
    }

    public String addDebt(AddDebtRequest addDebtRequest) {
        Optional<Company> companyCheck = companyRepository.findByCompanyCode(addDebtRequest.getCompanyCode());
        if (companyCheck.isEmpty()){
            return "company does not exits";
        }
        Company company = companyCheck.get();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Debt checkHistoryDebt = debtRepository.getLatestDebt(company.getId());
        if (checkHistoryDebt.getDate().getMonthValue()==now.getMonthValue()){
            Debt debt = new Debt();
            debt.setId(checkHistoryDebt.getId());
            debt.setDate(now);
            debt.setCompany(company);
            debt.setTotalMoney(checkHistoryDebt.getTotalMoney()+addDebtRequest.getTotalMoney());
            debt.setAmountPaid(checkHistoryDebt.getAmountPaid()+addDebtRequest.getAmountPaid());
            debt.setAmountOwed(checkHistoryDebt.getAmountOwed()+addDebtRequest.getAmountOwed());
            debtRepository.save(debt);
            return "success";
        }
        Debt debt = new Debt();
        debt.setId(checkHistoryDebt.getId());
        debt.setDate(now);
        debt.setCompany(company);
        debt.setTotalMoney(addDebtRequest.getTotalMoney());
        debt.setAmountPaid(addDebtRequest.getAmountPaid());
        debt.setAmountOwed(addDebtRequest.getAmountOwed());
        debtRepository.save(debt);
        return "success";
    }

}
