package tn.esprit.insurance.services;

import tn.esprit.insurance.entities.Sinister;
import tn.esprit.insurance.entities.TypeSin;

import java.util.List;
import java.util.Map;

public interface ISinisterService {


    Sinister updateSinister(Sinister sinister);
    String deleteSinister(int id_sin);
    List<Sinister> getAll();
    public Map<TypeSin, Long> countSinistersByType();

}
