package com.example.debtcontrolbe.model.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class CompanyDTO {
    private String companyCode;
    private String companyName;
    private String representativeName;
}
