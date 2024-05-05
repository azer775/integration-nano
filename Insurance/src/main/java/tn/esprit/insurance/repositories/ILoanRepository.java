package tn.esprit.insurance.repositories;

import org.springframework.data.repository.CrudRepository;
import tn.esprit.insurance.entities.Loan;

public interface ILoanRepository  extends CrudRepository<Loan, Integer> {
}
