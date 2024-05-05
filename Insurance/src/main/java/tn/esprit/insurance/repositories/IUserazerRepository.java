package tn.esprit.insurance.repositories;

import org.springframework.data.repository.CrudRepository;
import tn.esprit.insurance.entities.User;

public interface IUserazerRepository extends CrudRepository<User,Integer> {

}
