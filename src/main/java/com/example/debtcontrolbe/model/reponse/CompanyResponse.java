package com.example.debtcontrolbe.model.reponse;

import com.example.debtcontrolbe.model.dto.CompanyDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class CompanyResponse {
    private Integer total;
    private List<CompanyDTO> companyDTOList;
}
