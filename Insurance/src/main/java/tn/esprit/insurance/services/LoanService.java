package tn.esprit.insurance.services;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import tn.esprit.insurance.entities.Loan;
import tn.esprit.insurance.repositories.ILoanRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class LoanService implements ILoanService{
    ILoanRepository lr;

    public Loan addLoan(Loan l) {

        return (Loan)this.lr.save(l);
    }

    public Loan updateLoan(Loan Loan) {
        return (Loan)this.lr.save(Loan);
    }

    public void deleteLoan(int id) {
        this.lr.deleteById(id);
    }

    public List<Loan> getAll() {
        return (List<Loan>)this.lr.findAll();
    }

    public Loan getLoanById(int id) {
        return (Loan)this.lr.findById(id).orElse((Loan)null);
    }
    public void genererTableauAmortissement(double montantEmprunte, double tauxInteretAnnuel, int dureeMois, String cheminFichier) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet feuille = workbook.createSheet("Tableau d'amortissement");

        // Création des en-têtes
        Row enTete = feuille.createRow(0);
        enTete.createCell(0).setCellValue("Mois");
        enTete.createCell(1).setCellValue("Solde initial");
        enTete.createCell(2).setCellValue("Intérêts");
        enTete.createCell(3).setCellValue("Principal");
        enTete.createCell(4).setCellValue("Paiement mensuel");
        enTete.createCell(5).setCellValue("Solde final");

        double soldeInitial = montantEmprunte;
        double tauxInteretMensuel = tauxInteretAnnuel / 12 / 100;
        double interets = soldeInitial * tauxInteretMensuel;
        double paiementMensuel = interets*soldeInitial;//montantEmprunte * (tauxInteretMensuel / (1 - Math.pow(1 + tauxInteretMensuel, -dureeMois)));
        double principal = soldeInitial/dureeMois;//paiementMensuel - interets;
        double sommeInterets = 0;
        double sommePrincipal = 0;
        double sommePaiementMensuel = 0;
        for (int mois = 1; mois <= dureeMois; mois++) {
            Row ligne = feuille.createRow(mois);
            interets = soldeInitial * tauxInteretMensuel;
            double soldeFinal = soldeInitial - principal;
            paiementMensuel=principal+interets;
            ligne.createCell(0).setCellValue(mois);
            ligne.createCell(1).setCellValue(soldeInitial);
            ligne.createCell(2).setCellValue(interets);
            ligne.createCell(3).setCellValue(principal);
            ligne.createCell(4).setCellValue(paiementMensuel);
            ligne.createCell(5).setCellValue(soldeFinal);
            sommeInterets += interets;
            sommePrincipal += principal;
            sommePaiementMensuel += paiementMensuel;
            soldeInitial = soldeFinal;
        }
        Row derniere=feuille.createRow(dureeMois+1);
        derniere.createCell(2).setCellValue(sommeInterets);
        derniere.createCell(3).setCellValue(sommePrincipal);
        derniere.createCell(4).setCellValue(sommePaiementMensuel);
        FileOutputStream fichier = new FileOutputStream(cheminFichier);
        workbook.write(fichier);

        workbook.close();
        fichier.close();
    }
    public void genererTableauAmortissement1(double montantEmprunte, double tauxInteretAnnuel, int dureeMois, String cheminFichier) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet feuille = workbook.createSheet("Tableau d'amortissement");

        // Création des en-têtes
        Row enTete = feuille.createRow(0);
        enTete.createCell(0).setCellValue("Mois");
        enTete.createCell(1).setCellValue("Solde initial");
        enTete.createCell(2).setCellValue("Intérêts");
        enTete.createCell(3).setCellValue("Principal");
        enTete.createCell(4).setCellValue("Paiement mensuel");
        enTete.createCell(5).setCellValue("Solde final");

        double soldeInitial = montantEmprunte;
        double tauxInteretMensuel = tauxInteretAnnuel / 12 / 100;
        double paiementMensuel = montantEmprunte * (tauxInteretMensuel / (1 - Math.pow(1 + tauxInteretMensuel, -dureeMois)));

        for (int mois = 1; mois <= dureeMois; mois++) {
            Row ligne = feuille.createRow(mois);
            double interets = soldeInitial * tauxInteretMensuel;
            double principal = paiementMensuel - interets;
            double soldeFinal = soldeInitial - principal;

            ligne.createCell(0).setCellValue(mois);
            ligne.createCell(1).setCellValue(soldeInitial);
            ligne.createCell(2).setCellValue(interets);
            ligne.createCell(3).setCellValue(/*principal*/paiementMensuel);
            ligne.createCell(4).setCellValue(/*paiementMensuel*/principal);
            ligne.createCell(5).setCellValue(soldeFinal);

            soldeInitial = soldeFinal;
        }

        FileOutputStream fichier = new FileOutputStream(cheminFichier);
        workbook.write(fichier);

        workbook.close();
        fichier.close();
    }
    public void genererTableauAmortissement12(double montantEmprunte, double tauxInteretAnnuel, int dureeMois, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet feuille = workbook.createSheet("Tableau d'amortissement");

        // Création des en-têtes
        Row enTete = feuille.createRow(0);
        enTete.createCell(0).setCellValue("Mois");
        enTete.createCell(1).setCellValue("Solde initial");
        enTete.createCell(2).setCellValue("Intérêts");
        enTete.createCell(3).setCellValue("Principal");
        enTete.createCell(4).setCellValue("Paiement mensuel");
        enTete.createCell(5).setCellValue("Solde final");

        double soldeInitial = montantEmprunte;
        double tauxInteretMensuel = tauxInteretAnnuel / 12 / 100;
        double paiementMensuel = montantEmprunte * (tauxInteretMensuel / (1 - Math.pow(1 + tauxInteretMensuel, -dureeMois)));

        for (int mois = 1; mois <= dureeMois; mois++) {
            Row ligne = feuille.createRow(mois);
            double interets = soldeInitial * tauxInteretMensuel;
            double principal = paiementMensuel - interets;
            double soldeFinal = soldeInitial - principal;

            ligne.createCell(0).setCellValue(mois);
            ligne.createCell(1).setCellValue(soldeInitial);
            ligne.createCell(2).setCellValue(interets);
            ligne.createCell(3).setCellValue(paiementMensuel);
            ligne.createCell(4).setCellValue(principal);
            ligne.createCell(5).setCellValue(soldeFinal);

            soldeInitial = soldeFinal;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=tableau_amortissement.xlsx");

        ServletOutputStream servletOutputStream = response.getOutputStream();
        outputStream.writeTo(servletOutputStream);
        outputStream.close();
        servletOutputStream.flush();
        servletOutputStream.close();

        workbook.close();
    }
    public byte[] genererTableauAmortissement123(double montantEmprunte, double tauxInteretAnnuel, int dureeMois) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet feuille = workbook.createSheet("Tableau d'amortissement");

        // Création des en-têtes
        Row enTete = feuille.createRow(0);
        enTete.createCell(0).setCellValue("Mois");
        enTete.createCell(1).setCellValue("Solde initial");
        enTete.createCell(2).setCellValue("Intérêts");
        enTete.createCell(3).setCellValue("Principal");
        enTete.createCell(4).setCellValue("Paiement mensuel");
        enTete.createCell(5).setCellValue("Solde final");

        double soldeInitial = montantEmprunte;
        double tauxInteretMensuel = tauxInteretAnnuel / 12 / 100;
        double paiementMensuel = montantEmprunte * (tauxInteretMensuel / (1 - Math.pow(1 + tauxInteretMensuel, -dureeMois)));

        for (int mois = 1; mois <= dureeMois; mois++) {
            Row ligne = feuille.createRow(mois);
            double interets = soldeInitial * tauxInteretMensuel;
            double principal = paiementMensuel - interets;
            double soldeFinal = soldeInitial - principal;

            ligne.createCell(0).setCellValue(mois);
            ligne.createCell(1).setCellValue(soldeInitial);
            ligne.createCell(2).setCellValue(interets);
            ligne.createCell(3).setCellValue(paiementMensuel);
            ligne.createCell(4).setCellValue(principal);
            ligne.createCell(5).setCellValue(soldeFinal);

            soldeInitial = soldeFinal;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
    public byte[] genererTableauAmortissement12(double montantEmprunte, double tauxInteretAnnuel, int dureeMois) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet feuille = workbook.createSheet("Tableau d'amortissement");

            // Création des en-têtes
            Row enTete = feuille.createRow(0);
            enTete.createCell(0).setCellValue("Mois");
            enTete.createCell(1).setCellValue("Solde initial");
            enTete.createCell(2).setCellValue("Intérêts");
            enTete.createCell(3).setCellValue("Principal");
            enTete.createCell(4).setCellValue("Paiement mensuel");
            enTete.createCell(5).setCellValue("Solde final");

            double soldeInitial = montantEmprunte;
            double tauxInteretMensuel = tauxInteretAnnuel / 12 / 100;
            double paiementMensuel = montantEmprunte * (tauxInteretMensuel / (1 - Math.pow(1 + tauxInteretMensuel, -dureeMois)));

            for (int mois = 1; mois <= dureeMois; mois++) {
                Row ligne = feuille.createRow(mois);
                double interets = soldeInitial * tauxInteretMensuel;
                double principal = paiementMensuel - interets;
                double soldeFinal = soldeInitial - principal;

                ligne.createCell(0).setCellValue(mois);
                ligne.createCell(1).setCellValue(soldeInitial);
                ligne.createCell(2).setCellValue(interets);
                ligne.createCell(3).setCellValue(paiementMensuel);
                ligne.createCell(4).setCellValue(principal);
                ligne.createCell(5).setCellValue(soldeFinal);

                soldeInitial = soldeFinal;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return outputStream.toByteArray();
        }
    }

    public LoanService( ILoanRepository lr) {
        this.lr = lr;
    }
}
