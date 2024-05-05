package tn.esprit.insurance.services;

import org.springframework.stereotype.Service;
import tn.esprit.insurance.entities.Interview;
import tn.esprit.insurance.repositories.IInterviewRepository;

import java.util.List;
@Service
public class InterviewService implements IInterviewService{
    private  IInterviewRepository interviewRepository;
    @Override
    public Interview addInterview(Interview in) {
        return interviewRepository.save(in);
    }

    @Override
    public Interview updateInterview(Interview Interview) {
        return interviewRepository.save(Interview);
    }

    @Override
    public void deleteInterview(int id) {
        interviewRepository.deleteById(id);

    }

    @Override
    public List<Interview> getAll() {
        return (List<Interview>) interviewRepository.findAll();
    }

    @Override
    public Interview getInterviewById(int id) {
        return (Interview) interviewRepository.findById(id).orElse(null);
    }

    public InterviewService(IInterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }
}
