package com.example.debtcontrolbe.controller;


import com.example.debtcontrolbe.model.reponse.CompanyResponse;
import com.example.debtcontrolbe.model.reponse.FullCompanyInformationResponse;
import com.example.debtcontrolbe.model.request.AddCompanyRequest;
import com.example.debtcontrolbe.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    //get all company for manage page
    @GetMapping(value = "/get-company", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyResponse> getCompany(@RequestParam(name = "page") Integer page){
        return new ResponseEntity<>(companyService.getCompany(page), HttpStatus.OK);
    }

    //find all company with company code
    @GetMapping(value = "/get-company-by-company-code", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyResponse> getCompanyByCode(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "companyCode") String companyCode){
        return new ResponseEntity<>(companyService.getCompanyByCompanyCode(page,companyCode), HttpStatus.OK);
    }

    //get full company information by company code
    @GetMapping(value = "/get-full-company-info-by-company-code", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FullCompanyInformationResponse> getFullCompanyInfoByCode(
            @RequestParam(name = "companyCode") String companyCode){
        return new ResponseEntity<>(companyService.getFullCompanyInfoByCode(companyCode), HttpStatus.OK);
    }

    @PostMapping(value = "/add-company", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addCompany(
            @RequestBody AddCompanyRequest addCompanyRequest){
        return new ResponseEntity<>(companyService.addCompany(addCompanyRequest), HttpStatus.OK);
    }

    //http://localhost:8080/api/delete-company?companyCode=ABC
    @DeleteMapping(value = "/delete-company", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCompany(
            @RequestParam(name = "companyCode") String companyCode){
        return new ResponseEntity<>(companyService.deleteCompany(companyCode), HttpStatus.OK);
    }
}
