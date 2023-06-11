package com.example.debtcontrolbe.model.request;


import lombok.Data;

@Data
public class AddCompanyRequest {

    private String companyCode;

    private String nameCompany;

    private String representativeName;

    private String gmail;

    private String numberPhone;
}
