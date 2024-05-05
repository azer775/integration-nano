package tn.esprit.insurance.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.client.RestTemplate;
import tn.esprit.insurance.entities.Account;
import tn.esprit.insurance.entities.Devis;
import tn.esprit.insurance.entities.Insurance;
import tn.esprit.insurance.entities.TypeSin;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IDevisRepository extends CrudRepository<Devis,Integer> {

    /*@Query("SELECT DISTINCT a FROM Account a "
            + "JOIN a.transactionSet t "
            + "JOIN t.credits c "
            + "JOIN c.insurance i "
            + "GROUP BY a.IdAccount "
            + "HAVING COUNT(i.idinsurance) > 3")
    List<Account> findAccountsByNumberOfInsurances();*/

    @Query("SELECT i FROM Devis i WHERE i.endinsurance < :date")
    List<Devis> findByEndinsuranceBefore(@Param("date") Date date);

    @Query(value = "SELECT * FROM Devis u WHERE u.archived= true ", nativeQuery = true)
    List<Devis> retrieveArchivedinsurance();

    @Query("SELECT DISTINCT c FROM Devis c JOIN c.sinisters s WHERE s.typeSin = :type")
    List<Devis> selectDevisByType(@Param("type") TypeSin type);

    List<Devis> findByEndinsuranceBefore(LocalDate date);

    @Query("SELECT COUNT(d) FROM Devis d")
    int countDevis();

    @Query("SELECT d.insurance.typeAss, COUNT(d) FROM Devis d GROUP BY d.insurance.typeAss")
    List<Object[]> countDevisByInsuranceType();



}
