package com.example.debtcontrolbe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(unique = true)
    private String companyCode;

    @Column
    private String nameCompany;

    @OneToOne
    @JoinColumn(name = "representative_id")
    private Representative representative;

    public Company(String companyCode, String nameCompany, Representative representative) {
        this.companyCode = companyCode;
        this.nameCompany = nameCompany;
        this.representative = representative;
    }
}
