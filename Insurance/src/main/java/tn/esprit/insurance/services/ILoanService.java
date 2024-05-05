package tn.esprit.insurance.services;

import tn.esprit.insurance.entities.Loan;

import java.util.List;

public interface ILoanService {
    Loan addLoan(Loan ins);

    Loan updateLoan(Loan Loan);

    void deleteLoan(int id);

    List<Loan> getAll();

    Loan getLoanById(int id);
}
