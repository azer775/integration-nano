package tn.esprit.insurance.services;

import tn.esprit.insurance.entities.Devis;
import tn.esprit.insurance.entities.ExcelData;
import tn.esprit.insurance.entities.Insurance;
import tn.esprit.insurance.entities.TypeSin;

import java.io.IOException;
import java.util.List;

public interface IDevisService {

    Devis updateDevis(Devis dev);
    void deleteDevis(int idDev);
    List<Devis> getAll();
    Devis getDevisById(int idDev);

    List<Devis> retrieveArchivedinsurance();

    List<Devis> getDevisForAssurance(int id_ass);

    Devis addDevtoIns(Devis devis, int id_ass) throws IOException;

    List<Devis> FilterDevis(TypeSin type);
}
