package com.example.debtcontrolbe.service;

import com.example.debtcontrolbe.model.Company;
import com.example.debtcontrolbe.model.dto.CompanyDTO;
import com.example.debtcontrolbe.model.reponse.CompanyResponse;
import com.example.debtcontrolbe.model.reponse.FullCompanyInformationResponse;
import com.example.debtcontrolbe.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private List<Company> companyList = new ArrayList<>();

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyResponse getCompany(Integer page) {
        int index = page*10;
        if (companyList.isEmpty()){
            companyList = companyRepository.findAll();
        }
        int start = index-10;
        int end = index;

        if (end> companyList.size()){
            end = companyList.size();
        }

        return new CompanyResponse(companyList.size(),companyList.subList(start,end).stream().map(i -> new CompanyDTO(i.getCompanyCode(),i.getNameCompany(),i.getRepresentative().getRepresentativeName())).collect(Collectors.toList()));
    }

    public CompanyResponse getCompanyByCompanyCode(Integer page,String companyCode) {
        int index = page*10;

        List<Company> companyListByCode = companyRepository.findByCompanyCodeContains(companyCode);

        int start = index-10;
        int end = index;

        if (end> companyListByCode.size()){
            end = companyListByCode.size();
        }

        return new CompanyResponse(
                companyListByCode.size(),
                companyListByCode.subList(start,end).stream().map(i -> new CompanyDTO(i.getCompanyCode(),i.getNameCompany(),i.getRepresentative().getRepresentativeName())).collect(Collectors.toList()));
    }

    public FullCompanyInformationResponse getFullCompanyInfoByCode(String companyCode) {
        Company company = companyRepository.findAllByCompanyCode(companyCode);
        return new FullCompanyInformationResponse(
                company.getCompanyCode(),
                company.getNameCompany(),
                company.getRepresentative().getId(),
                company.getRepresentative().getRepresentativeName(),
                company.getRepresentative().getGmail(),
                company.getRepresentative().getNumberPhone()
        );
    }
}
