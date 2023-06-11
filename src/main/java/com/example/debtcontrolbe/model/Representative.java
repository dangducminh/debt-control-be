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


    public Representative(String representativeName, String gmail, String numberPhone) {
        this.representativeName = representativeName;
        this.gmail = gmail;
        this.numberPhone = numberPhone;
    }

}
