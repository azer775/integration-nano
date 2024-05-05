package tn.esprit.insurance.services;

import tn.esprit.insurance.entities.Interview;
import tn.esprit.insurance.entities.Loan;

import java.util.List;

public interface IInterviewService {
    Interview addInterview(Interview in);

    Interview updateInterview(Interview Interview);

    void deleteInterview(int id);

    List<Interview> getAll();

    Interview getInterviewById(int id);
}
