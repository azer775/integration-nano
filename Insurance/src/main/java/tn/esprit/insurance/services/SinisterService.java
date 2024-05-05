package tn.esprit.insurance.services;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.insurance.entities.*;
import tn.esprit.insurance.repositories.IDevisRepository;
import tn.esprit.insurance.repositories.IInsuranceRepository;
import tn.esprit.insurance.repositories.ISinisterRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SinisterService implements ISinisterService{

    private final ISinisterRepository sinisterRepository;
    private final IDevisRepository devisRepository;
    private final IInsuranceRepository insuranceRepository;

    public Sinister addSinister(Sinister sinister)
    {
        return sinisterRepository.save(sinister);
    }

    @Override
    public Sinister updateSinister(Sinister sinister) {
        return sinisterRepository.save(sinister);
    }

    @Override
    public String deleteSinister(int id_sin)
    {
        sinisterRepository.deleteById(id_sin);
        return "Sinister removed ! " +id_sin;
    }

    @Override
    public List<Sinister> getAll()
    {
        return (List<Sinister>) sinisterRepository.findAll() ;
    }


    public boolean affecterSinistre(Sinister sin, int idDev) {
        // Récupérer le devis à partir de son identifiant
        Optional<Devis> optionalDevis = devisRepository.findById(idDev);
        if (optionalDevis.isEmpty()) {
            return false; // Le devis n'existe pas
        }

        Devis devis = optionalDevis.get();

        // Vérifier si le nombre de sinistres affectés à ce devis est supérieur à 3
        long countSinisters = sinisterRepository.countByDevis(devis);
        if (countSinisters >= 3) {
            // Si le nombre de sinistres dépasse 3, annuler le devis et le supprimer
            devis.setStatPol(StatPol.ANNULE);
            devisRepository.save(devis);
            //devisRepository.delete(devis);
            return false;
        }

        // Ajouter le sinistre au devis
        sin.setDevis(devis);
        sinisterRepository.save(sin);
        return true;

    }


    public String saveFile(MultipartFile file, String prefix) throws IOException {
        String destinationDirectory="C:/Users/Ryhab/Desktop/data";
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = generateUniqueFileName(prefix, extension);
        File destFile = new File(destinationDirectory + File.separator + newFileName);
        file.transferTo(destFile);
        return destinationDirectory+"/"+newFileName;
    }

    private String generateUniqueFileName(String prefix, String extension) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedDateTime = now.format(formatter);
        return prefix + "_" + formattedDateTime + extension;
    }

    public Map<TypeSin, Long> countSinistersByType() {
        List<Object[]> results = sinisterRepository.countSinistersByType();
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (TypeSin) row[0],
                        row -> (Long) row[1]));
    }

    public int countDevis() {
        return devisRepository.countDevis();
    }

    public List<Object[]> countDevisByInsuranceType() {
        return devisRepository.countDevisByInsuranceType();
    }

    public long countSinisters() {
        return sinisterRepository.countSinisters();
    }

    public long countInsurances() {
        return insuranceRepository.countInsurances();
    }

    public Sinister modifyStat(int id_sin)
    {
        Sinister s= sinisterRepository.findById(id_sin).orElse(null);
        s.setStatut_sin(Statut_Sin.settled);
        return sinisterRepository.save(s);
    }

    public List<Sinister> getSinByDev(int idDev){
        return sinisterRepository.findByDevisIdDev(idDev);
    }
}

