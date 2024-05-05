package tn.esprit.insurance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.insurance.entities.Loan;
import tn.esprit.insurance.services.CreditScoringService;
import tn.esprit.insurance.services.LoanService;

@RestController
@RequestMapping("/scoring")
public class CreditScoringController {
    @Autowired
    CreditScoringService cs;
    @Autowired
    LoanService ls;

    @GetMapping({"/creditscore/{id}"})
    public double delete(@PathVariable int id) {
        Loan loan=ls.getLoanById(id);
        return cs.calculateCreditScore(loan);
    }
}
