package com.example.debtcontrolbe.service;

import com.example.debtcontrolbe.model.Company;
import com.example.debtcontrolbe.model.Representative;
import com.example.debtcontrolbe.model.dto.CompanyDTO;
import com.example.debtcontrolbe.model.reponse.CompanyResponse;
import com.example.debtcontrolbe.model.reponse.FullCompanyInformationResponse;
import com.example.debtcontrolbe.model.request.AddCompanyRequest;
import com.example.debtcontrolbe.repository.CompanyRepository;
import com.example.debtcontrolbe.repository.DebtRepository;
import com.example.debtcontrolbe.repository.RepresentativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RepresentativeRepository representativeRepository;

    @Autowired
    private DebtRepository debtRepository;


    public CompanyResponse getCompany(Integer page) {
        List<Company> companyList = companyRepository.findAll();
        int index = page*10;
        int start = index-10;
        int end = index;

        if(companyList.size()<10){
            return new CompanyResponse(
                    companyList.size(),
                    companyList.stream().map(i -> new CompanyDTO(i.getCompanyCode(),i.getNameCompany(),i.getRepresentative().getRepresentativeName())).collect(Collectors.toList()));
        }else if(start>companyList.size()){
            return (CompanyResponse) Collections.emptyList();
        }else if (end> companyList.size()){
            end = companyList.size();
        }

        return new CompanyResponse(companyList.size(),companyList.subList(start,end).stream().map(i -> new CompanyDTO(i.getCompanyCode(),i.getNameCompany(),i.getRepresentative().getRepresentativeName())).collect(Collectors.toList()));
    }

    public CompanyResponse getCompanyByCompanyCode(Integer page,String companyCode) {
        int index = page*10;

        List<Company> companyListByCode = companyRepository.findByCompanyCodeContains(companyCode);

        int start = index-10;
        int end = index;

        if(companyListByCode.size()<10){
            return new CompanyResponse(
                    companyListByCode.size(),
                    companyListByCode.stream().map(i -> new CompanyDTO(i.getCompanyCode(),i.getNameCompany(),i.getRepresentative().getRepresentativeName())).collect(Collectors.toList()));
        } else if(start>companyListByCode.size()){
            return (CompanyResponse) Collections.emptyList();
        }else if (end> companyListByCode.size()){
            end = companyListByCode.size();
        }

        return new CompanyResponse(
                companyListByCode.size(),
                companyListByCode.subList(start,end).stream().map(i -> new CompanyDTO(i.getCompanyCode(),i.getNameCompany(),i.getRepresentative().getRepresentativeName())).collect(Collectors.toList()));
    }

    public FullCompanyInformationResponse getFullCompanyInfoByCode(String companyCode) {
        Company company = companyRepository.findByCompanyCode(companyCode).get();
        return new FullCompanyInformationResponse(
                company.getCompanyCode(),
                company.getNameCompany(),
                company.getRepresentative().getId(),
                company.getRepresentative().getRepresentativeName(),
                company.getRepresentative().getGmail(),
                company.getRepresentative().getNumberPhone()
        );
    }


    public String addCompany(AddCompanyRequest addCompanyRequest) {
        Optional<Company> companies = companyRepository.findByCompanyCode(addCompanyRequest.getCompanyCode());
        if (companies.isEmpty()){
            Representative representative = new Representative(addCompanyRequest.getRepresentativeName(),addCompanyRequest.getGmail(),addCompanyRequest.getNumberPhone());
            Company company = new Company(addCompanyRequest.getCompanyCode(),addCompanyRequest.getNameCompany(),representative);
            representativeRepository.save(representative);
            companyRepository.save(company);
            return "save company success";
        }
        return "company exist";
    }

    public String deleteCompany(String companyCode) {

        Optional<Company> companies = companyRepository.findByCompanyCode(companyCode);
        if (companies.isPresent()){
            Company company = companies.get();
            debtRepository.deleteByCompanyId(company.getId());
            companyRepository.deleteByCompanyCode(companyCode);
            representativeRepository.deleteById(company.getRepresentative().getId());
            return "Success";
        }
        return "company not exist";
    }
}
