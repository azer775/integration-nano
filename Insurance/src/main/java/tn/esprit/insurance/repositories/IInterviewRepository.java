package tn.esprit.insurance.repositories;

import org.springframework.data.repository.CrudRepository;
import tn.esprit.insurance.entities.Interview;

public interface IInterviewRepository extends CrudRepository<Interview, Integer> {
}
