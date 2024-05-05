package tn.esprit.insurance.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.insurance.entities.Insurance;
import tn.esprit.insurance.entities.TypeAss;

import java.util.List;

public interface IInsuranceRepository extends CrudRepository<Insurance,Integer>, JpaSpecificationExecutor<Insurance> {

    List<Insurance> findByTypeAss(TypeAss typeAss);
    @Query("SELECT COUNT(i) FROM Insurance i")
    int countInsurances();


}
