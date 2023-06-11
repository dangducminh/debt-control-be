package com.example.debtcontrolbe.controller;

import com.example.debtcontrolbe.model.Debt;
import com.example.debtcontrolbe.model.reponse.FullCompanyInformationResponse;
import com.example.debtcontrolbe.model.reponse.HistoryDebtResponse;
import com.example.debtcontrolbe.model.request.AddDebtRequest;
import com.example.debtcontrolbe.service.DebtService;
import com.example.debtcontrolbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DebtController {

    @Autowired
    private DebtService debtService;

    @GetMapping(value = "/get-debt-history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HistoryDebtResponse>> getDebtHistory(
            @RequestParam(name = "companyCode") String companyCode){
        return new ResponseEntity<>(debtService.getDebtHistory(companyCode), HttpStatus.OK);
    }


    @PostMapping(value = "/add-debt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addDebt(
            @RequestBody AddDebtRequest debt){
        return new ResponseEntity<String>(debtService.addDebt(debt), HttpStatus.OK);
    }
}
