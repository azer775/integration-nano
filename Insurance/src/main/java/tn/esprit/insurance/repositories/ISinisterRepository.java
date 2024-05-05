package tn.esprit.insurance.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.insurance.entities.Devis;
import tn.esprit.insurance.entities.Sinister;

import java.util.List;

public interface ISinisterRepository extends CrudRepository<Sinister,Integer> {

    int countByDevis(Devis devis);

    List<Sinister> findByDevisIdDev(int idDev);
    List<Sinister> findByDevis(Devis devis);
    @Query("SELECT s.typeSin, COUNT(s) FROM Sinister s GROUP BY s.typeSin")
    List<Object[]> countSinistersByType();
    @Query("SELECT COUNT(s) FROM Sinister s")
    int countSinisters();

}
