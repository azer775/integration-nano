package tn.esprit.insurance.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.insurance.entities.Profil;

import java.util.Optional;

public interface ProfilRepository extends JpaRepository<Profil, Integer> {
    Optional<Profil> findByUserId(int userId);
}
