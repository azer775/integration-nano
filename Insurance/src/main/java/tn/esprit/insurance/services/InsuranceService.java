package tn.esprit.insurance.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.insurance.entities.ExcelData;
import tn.esprit.insurance.entities.Insurance;
import tn.esprit.insurance.entities.Sinister;
import tn.esprit.insurance.entities.TypeAss;
import tn.esprit.insurance.repositories.IInsuranceRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InsuranceService implements IInsuranceService{

    private IInsuranceRepository insuranceRepository;

    public Insurance addInsurance(Insurance insurance)
    {
        return insuranceRepository.save(insurance);
    }

    public Insurance updateInsurance(Insurance insurance)
    {
        return insuranceRepository.save(insurance);
    }

    public void deleteInsurance(int id_ass)
    {
        insuranceRepository.deleteById(id_ass);

    }

    public List<Insurance> getAll()
    {
        return (List<Insurance>) insuranceRepository.findAll();
    }

    public Insurance getInsuranceById(int id_ass)
    {
        return insuranceRepository.findById(id_ass).orElse(null);
    }


    public void saveInsFromExcel(List<ExcelData> excelDataList) {
        for (ExcelData excelData : excelDataList) {
            Insurance insurance = transformToIns(excelData); // Transform ExcelData to Offer
            insuranceRepository.save(insurance);
        }
    }

    private Insurance transformToIns(ExcelData excelData) {
        Insurance insurance = new Insurance();
        insurance.setCeiling(excelData.getCeiling());
        insurance.setConditions(excelData.getConditions());
        insurance.setDescription(excelData.getDescription());
        insurance.setEvent_ass(excelData.getEvent_ass());
        insurance.setExclusions(excelData.getExclusions());
        insurance.setName(excelData.getName());
        insurance.setTypeAss(excelData.getTypeAss());
        return insurance;
    }
    

    public String generateAssuranceReport(int id_ass) {
        // Récupérer l'assurance à partir de son ID
        Insurance ins = insuranceRepository.findById(id_ass)
                .orElseThrow(() -> new IllegalArgumentException("Insurance not found"));

        // Générer le rapport détaillé
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Détails de l'assurance :\n")
                .append("Nom de l'assurance : ").append(ins.getName()).append("\n")
                .append("Type d'assurance : ").append(ins.getTypeAss()).append("\n")
                .append("Description : ").append(ins.getDescription()).append("\n")
                .append("Ceiling : ").append(ins.getCeiling()).append("\n")
                .append("Evenement d'assurance : ").append(ins.getEvent_ass()).append("\n")
                .append("Les conditions : ").append(ins.getConditions()).append("\n")
                .append("Les exclusions : ").append(ins.getExclusions()).append("\n")
                // Ajouter d'autres informations pertinentes de l'assurance
                .append("\n\n");

        return reportBuilder.toString();
    }



    public TypeAss getTypeAssById(int id_ass) {
        Insurance insurance = insuranceRepository.findById(id_ass).orElse(null);
        if (insurance != null) {
            return insurance.getTypeAss();
        } else {
            // Gérer le cas où aucune assurance n'est trouvée pour l'ID donné
            return null;
        }
    }




}
