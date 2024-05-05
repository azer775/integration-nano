package tn.esprit.insurance.repositories;

import org.springframework.data.repository.CrudRepository;
import tn.esprit.insurance.entities.Pack;

public interface IPackRepository extends CrudRepository<Pack, Integer> {
}
