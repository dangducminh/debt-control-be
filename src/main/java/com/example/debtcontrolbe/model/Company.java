package com.example.debtcontrolbe.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
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
}
