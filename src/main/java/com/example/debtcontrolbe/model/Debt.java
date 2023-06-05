package com.example.debtcontrolbe.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "company_code")
    private Company company;

    @Column
    private Double totalMoney;

    @Column
    private Double amountPaid;

    @Column
    private Double amountOwed;

    @Column
    private LocalDateTime date;
}
