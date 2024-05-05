package tn.esprit.insurance.services;

import tn.esprit.insurance.entities.Recoveries;

import java.util.List;

public interface IRecoveriesService {
    Recoveries addRecoveries(Recoveries ins);

    Recoveries updateRecoveries(Recoveries Recoveries);

    void deleteRecoveries(int id);

    List<Recoveries> getAll();

    Recoveries getRecoveriesById(int id);
}
