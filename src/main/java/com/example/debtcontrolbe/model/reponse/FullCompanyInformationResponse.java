package com.example.debtcontrolbe.model.reponse;

import lombok.*;



@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class FullCompanyInformationResponse {
    private String companyCode;
    private String companyName;
    private Long representativeId;
    private String representativeName;
    private String gmail;
    private String numberPhone;
}
