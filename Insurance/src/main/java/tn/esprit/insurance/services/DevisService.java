package tn.esprit.insurance.services;

/*import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.property.TextAlignment;*/
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.insurance.entities.*;
import tn.esprit.insurance.repositories.IDevisRepository;
import tn.esprit.insurance.repositories.IInsuranceRepository;

import tn.esprit.insurance.repositories.ISinisterRepository;
import tn.esprit.insurance.entities.TypeAss;


import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class DevisService implements IDevisService{

    private final IDevisRepository devisRepository;
    private final ISinisterRepository sinisterRepository;
    private final IInsuranceRepository insuranceRepository;
    private final NotificationService notificationService;

    //CRUDs
    @Override
    public Devis addDevtoIns(Devis devis, int id_ass) throws IOException {
        Insurance insurance = insuranceRepository.findById(id_ass).orElse(null);
        devis.setInsurance(insurance);
        // Définir la date de début d'assurance sur la date actuelle
        devis.setStartinsurance(LocalDate.now());

        // Définir la date de fin d'assurance sur un an après la date actuelle
        devis.setEndinsurance(LocalDate.now().plusYears(3));
        CompletableFuture<Double> primeFuture = CompletableFuture.supplyAsync(() -> calculerPrime(devis));

        // Attendez la fin du calcul de la prime
        try {
            double prime = primeFuture.get();
            devis.setPrime(prime);
        } catch (Exception e) {
            e.printStackTrace(); // Gérer les erreurs
        }

        return devisRepository.save(devis);

    }

    @Override
    public Devis updateDevis(Devis devis)
    {
        return devisRepository.save(devis);
    }
    @Override
    public void deleteDevis(int idDev)
    {
        devisRepository.deleteById(idDev);

    }
    @Override
    public List<Devis> getAll()
    {
        return (List<Devis>) devisRepository.findAll();
    }

    public Devis getDevisById(int idDev)
    {
        return devisRepository.findById(idDev).orElse(null);
    }

    @Override
    public List<Devis> retrieveArchivedinsurance() {
        return (List<Devis>) devisRepository.retrieveArchivedinsurance();
    }

    @Override
    public List<Devis> getDevisForAssurance(int id_ass) {
        List<Devis> allDevisForAssurance = new ArrayList<>();

        // Récupérer l'assurance associée à l'identifiant spécifié
        Optional<Insurance> assuranceOptional = insuranceRepository.findById(id_ass);
        assuranceOptional.ifPresent(assurance -> {
            // Récupérer la liste des devis associés à cette assurance
            Set<Devis> devisList = assurance.getDevisSet();
            // Ajouter les devis à la liste globale
            allDevisForAssurance.addAll(devisList);
        });

        return allDevisForAssurance;
    }

    //******************************************************** CALCUL
    //Calcul de nombre de mois entre deux dates données
    private long getMonthsBetweenDates(LocalDate startinsurance, LocalDate endinsurance) {
        return ChronoUnit.MONTHS.between(startinsurance, endinsurance);
    }


    //******************************************** archive les assurances expirées
    @Scheduled(cron = "0 0 0 * * *")
    public void archiveExpiredInsurances() {
        // Récupérer les assurances dont la date de fin est dépassée
        List<Devis> expiredInsurances = devisRepository.findByEndinsuranceBefore(new Date());
        for (Devis devis : expiredInsurances) {
            System.out.println(devis.getIdDev());
            // Mettre à jour la date de fin d'assurance avec la date actuelle
            // devis.setEndinsurance(new Date());
            devis.setArchived(true);
            // Enregistrer la modification dans la base de données
            devisRepository.save(devis);
        }
    }


    //Renouvellement auto d'assurance
    public void renouvellementAutomatique(int idDev) {
        // Récupérer l'assurance à partir de son ID
        Devis devis = devisRepository.findById(idDev)
                .orElseThrow(() -> new RuntimeException("Assurance non trouvée avec l'ID : " + idDev));

        // Vérifier si la date d'expiration est proche
        LocalDate dateExpiration = devis.getEndinsurance();
        LocalDate dateJour = LocalDate.now();
        if (dateExpiration.minusDays(30).isBefore(dateJour)) {
            // Mettre en œuvre la logique de renouvellement automatique
            // Par exemple, ajouter une année à la date d'expiration
            LocalDate nouvelleDateExpiration = dateExpiration.plusYears(1);
            devis.setEndinsurance(nouvelleDateExpiration);
            String senderPhoneNumber = "+21658519140";
            String message = String.format("L'assurance a été renouvelée automatiquement pour une année supplémentaire.");
            notificationService.sendSMS(senderPhoneNumber, message);
            // Enregistrer les modifications
            devisRepository.save(devis);

            // Optionnel : envoyer un e-mail de confirmation, notifier l'utilisateur, etc.
            System.out.println("L'assurance a été renouvelée automatiquement pour une année supplémentaire.");
        } else {
            System.out.println("La date d'expiration est encore loin, aucun renouvellement automatique nécessaire.");
        }
    }


    //********************************************************* FILTRE
    @Override
    public List<Devis> FilterDevis(TypeSin type) {
        return devisRepository.selectDevisByType(type);
    }

    // ********************************************************  Generer un contrat pdf
    public File genererContratPDF(Devis devis) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer =PdfWriter.getInstance(document, new FileOutputStream("devis.pdf"));
        document.open();
        // Logo
        Image logo = Image.getInstance("C:/Users/Ryhab/Desktop/PI/logo.png");
        logo.scaleAbsolute(90f, 90f);
        logo.setAlignment(Element.ALIGN_RIGHT);
        document.add(logo);
        //Titre
        Paragraph title = new Paragraph("NanoCash microFinance et assurance \n\n\n", new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("1.Parties au devis : \n\n", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        document.add(new Paragraph("Assureur : nanoCash microFinance et assurance "));
        //document.add(new Paragraph("Souscripteur : "+ contract.getUser().getLastName() +" "+contract.getUser().getFirstName()));
        document.add(new Paragraph("\n2.Objet du devis : \n\n", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        document.add(new Paragraph("Type d'assurance :"+ devis.getInsurance().getTypeAss()));
        document.add(new Paragraph("Montant de la garantie :"+ devis.getInsurance().getCeiling()));
        document.add(new Paragraph("\n3.Exclusions de garanties :\n\n", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        document.add(new Paragraph(" "+ devis.getInsurance().getExclusions()));
        document.add(new Paragraph("\n3.Conditions de garanties :\n\n", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        document.add(new Paragraph(" "+ devis.getInsurance().getConditions()));
        document.add(new Paragraph("\n3.Evenements assurés :\n\n", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        document.add(new Paragraph(" "+ devis.getInsurance().getEvent_ass()));
        document.add(new Paragraph("\n4.Durée du contrat :\n\n", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        document.add(new Paragraph("Ce contrat est valide du " + devis.getStartinsurance()+" Jusqu'au "+ devis.getEndinsurance()));
        document.add(new Paragraph("\n5. A propos du Contrat :\n\n", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
        document.add(new Paragraph("Prime :"+ devis.getPrime()));
        document.add(new Paragraph("Statut du Contrat : "+ devis.getStatPol()));

        // Adding footer with page number and date
        /*Paragraph footer = new Paragraph("Page " + writer.getPageNumber() + " | " + new Date().toString(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);*/
        Paragraph footer = new Paragraph("\n\n\n\n\n\n\n\n\n\n\nPage " + writer.getPageNumber() + " | " + new Date().toString() +"| \nNous vous rappelons que les informations communiquées dans ce contrat sont confidentielles et " +
                "réservées à l'usage exclusif de l'assureur et du souscripteur. " +
                "Conformément à la réglementation en vigueur, vous disposez d'un droit d'accès, de rectification et d'opposition pour" +
                " toute information vous concernant. Pour exercer ces droits, vous pouvez nous contacter à l'adresse suivante [NanoCash@gamil.com]." +
                " Ce contrat est régi par la loi française et tout litige en découlant sera soumis à la compétence des tribunaux tunisiens.", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        // Closing the document
        document.close();
        return null;
    }


    //*******************************************  RAPPORT
    public String generateAssuranceReport(int idDev) {
        // Récupérer le devis/assurance à partir de son ID
        Devis devis = devisRepository.findById(idDev)
                .orElseThrow(() -> new IllegalArgumentException("Devis not found"));

        // Récupérer la liste des sinistres associés à cet ID de devis
        List<Sinister> sinistres = sinisterRepository.findByDevisIdDev(idDev);

        // Générer le rapport détaillé pour chaque sinistre
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Détails de l'assurance :\n")
                .append("Nom de l'assurance : ")
                .append(devis.getInsurance().getName()).append("\n")
                .append("Type d'assurance : ")
                .append(devis.getInsurance().getTypeAss()).append("\n")
                // Ajouter d'autres informations pertinentes de l'assurance
                .append("\n\n");

        // Générer un rapport détaillé pour chaque sinistre
        for (Sinister sinistre : sinistres) {
            reportBuilder.append("Détails du sinistre :\n")
                    .append("Date du sinistre : ").append(sinistre.getDate_sin()).append("\n")
                    .append("Montant : ").append(sinistre.getAmount()).append("\n")
                    .append("Type du sinistre : ").append(sinistre.getTypeSin()).append("\n")
                    // Ajouter d'autres informations pertinentes du sinistre
                    .append("\n");
        }

        return reportBuilder.toString();
    }


    public double calculateRiskScore(int idDev) {
        Devis devis = devisRepository.findById(idDev).orElse(null);
        if (devis == null) {
            // Gérer le cas où le devis n'est pas trouvé
            return 0.0; // ou une autre valeur par défaut
        }
        double maxRiskScore = 100.0; // Définir la valeur maximale possible pour le score de risque
        double riskScorePercentage = 0.0;

        if (devis != null) {
            double riskScore = 0.0;
            switch (devis.getInsurance().getTypeAss()) {
                case VOITURE:
                    riskScore += devis.getPuissance() * 0.001;
                    riskScore += devis.getBonus_malus() * 0.005;
                    riskScore += devis.getValneuf() * 0.002;
                    riskScore += devis.getValvenal() * 0.001;
                    break;
                case HABITATION:
                    riskScore += devis.getSuperficietot() * 0.005;
                    riskScore += devis.getNbrchambre() * 0.01;
                    riskScore += devis.getCapitalmob() * 0.002;
                    riskScore += devis.getCapitaldamage() * 0.001;
                    break;
                // Ajoutez d'autres cas pour d'autres types de polices d'assurance si nécessaire
                default:
                    break;
            }

            // Calcul du score de risque en pourcentage
            riskScorePercentage = (riskScore / maxRiskScore) * 10.0;

        }

        return riskScorePercentage;
    }





    public int calculateExpectedClaims(int idDev) {
        Devis devis = devisRepository.findById(idDev).orElse(null);
        if (devis == null) {
            // Gérer le cas où le devis n'est pas trouvé
            return -1; // ou une autre valeur par défaut
        }

        int expectedClaims = 0;

        // Estimation des sinistres attendus en fonction du type d'assurance
        if (devis.getInsurance() != null) {
            TypeAss insuranceType = devis.getInsurance().getTypeAss();

            // Si c'est une assurance voiture
            if (insuranceType == TypeAss.VOITURE) {
                // Estimation basée sur la puissance du véhicule
                if (devis.getPuissance() > 200) {
                    expectedClaims += 4; // Véhicules très puissants : plus de sinistres attendus
                } else if (devis.getPuissance() > 150) {
                    expectedClaims += 3; // Véhicules assez puissants : quelques sinistres attendus
                } else {
                    expectedClaims += 1; // Véhicules moins puissants : moins de sinistres attendus
                }

                // Estimation basée sur le bonus-malus
                if (devis.getBonus_malus() >= 7) {
                    expectedClaims += 3; // Bonus-malus élevé : plus de sinistres attendus
                } else if (devis.getBonus_malus() >= 3) {
                    expectedClaims += 2; // Bonus-malus moyen : quelques sinistres attendus
                }
            }
            // Si c'est une assurance habitation
            else if (insuranceType == TypeAss.HABITATION) {
                // Estimation basée sur la superficie totale de l'habitation
                if (devis.getSuperficietot() > 300) {
                    expectedClaims += 5; // Grandes habitations : plus de sinistres attendus
                } else if (devis.getSuperficietot() > 200) {
                    expectedClaims += 3; // Habitats de taille moyenne : quelques sinistres attendus
                }

                // Estimation basée sur le nombre de chambres
                expectedClaims += devis.getNbrchambre() * 2; // Plus de chambres : plus de sinistres attendus
            }
            else if (insuranceType == TypeAss.BATEAU) {
                if(devis.getNbr_chevaux() > 100) {
                    expectedClaims += 2;
                } else if (devis.getNbr_chevaux() > 300) {
                    expectedClaims += 4;
                }
                if (devis.getNbr_places() > 50) {
                    expectedClaims += 1;
                } else if (devis.getNbr_places() > 300) {
                    expectedClaims += 6;
                }
            }
            // Ajoutez d'autres conditions pour d'autres types d'assurance si nécessaire
        }

        return expectedClaims;
    }

    public double calculerValeurRachat(int idDev) {
        // Récupérer le devis correspondant à l'ID
        Devis devis = devisRepository.findById(idDev).orElse(null);

        // Vérifier si le devis existe
        if (devis == null) {
            // Gérer le cas où le devis n'est pas trouvé
            return 0.0; // ou une autre valeur par défaut
        }

        // Récupérer les informations pertinentes du devis
        double prime = devis.getPrime();
        LocalDate debutContrat = devis.getStartinsurance();
        LocalDate finContrat = devis.getEndinsurance();

        // Calculer la durée du contrat en années
        long dureeContratEnAnnees = ChronoUnit.YEARS.between(debutContrat, finContrat);

        // Calculer la valeur accumulée (somme des primes versées)
        double valeurAccumulee = prime;

        // Appliquer un taux de rachat (exemple : 90% de la valeur accumulée)
        double tauxRachat = 0.9; // 90%
        double valeurRachat = valeurAccumulee * tauxRachat;

        return valeurRachat;
    }


    // Méthode pour calculer la prime d'assurance
    public double calculerPrime(Devis devis) {
        double prime = 0;

        // Switch sur le type d'assurance
        switch (devis.getInsurance().getTypeAss()) {
            case TypeAss.VOITURE:
                prime += devis.getPuissance() * 6;
                break;
            case HABITATION:
                prime += devis.getSuperficiech() * 10;
                break;
            case BATEAU:
                prime += devis.getVal_bat() * 0.5;
            case VOYAGE:
                prime += devis.getBillet() * 0.2;
            case VIE:
                prime += 10000.0;
            case CREDIT:
                prime += 4000.0;
            default:
                prime += 6000.0;
        }
        return prime;
    }

    public Devis getDevById(int idDev)
    {
        return devisRepository.findById(idDev).orElse(null);
    }

    public Devis getDevisDetails(int idDev) {
        Devis devis = devisRepository.findById(idDev).orElse(null);
        if (devis != null) {
            Insurance insurance = devis.getInsurance();
            Set<Sinister> sinistres = (Set<Sinister>) sinisterRepository.findByDevis(devis);
            return new Devis(devis, insurance, sinistres);
        } else {
            return null;
        }
    }



}






