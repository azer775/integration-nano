package tn.esprit.insurance.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.insurance.entities.User;

public interface UserRepository extends JpaRepository <User, Integer> {
    User findByEmail(String email);
    User findByEmailAndPassword(String Email,String Password);

    User findByFirstname(String firstname);
}
