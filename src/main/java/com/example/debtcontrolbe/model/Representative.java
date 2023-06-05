package com.example.debtcontrolbe.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Representative {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column
    private String representativeName;

    @Column(unique = true)
    private String gmail;

    @Column(unique = true)
    private String numberPhone;

}
