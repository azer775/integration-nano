package tn.esprit.insurance.services;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import tn.esprit.insurance.entities.Loan;
import tn.esprit.insurance.entities.Recoveries;
import tn.esprit.insurance.repositories.ILoanRepository;
import tn.esprit.insurance.repositories.IRecoveriesRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class RecoveriesService implements IRecoveriesService{
    IRecoveriesRepository rr;
    @Override
    public Recoveries addRecoveries(Recoveries r) {
        return (Recoveries) this.rr.save(r);
    }

    @Override
    public Recoveries updateRecoveries(Recoveries r) {
        return (Recoveries) this.rr.save(r);
    }

    @Override
    public void deleteRecoveries(int id) {
        this.rr.deleteById(id);
    }

    @Override
    public List<Recoveries> getAll() {
        return (List<Recoveries>) this.rr.findAll();
    }
    public List<Recoveries> validateloan(Loan loan){
        //LocalDate startDate = (LocalDate) loan.getStart();
        List<Recoveries> recoveriesList = new ArrayList<>();
        double soldeInitial = loan.getAmount();
        float tauxInteretMensuel = /*loan.getPack().getInterest()*/(float) 20 / 12 / 100;
        double paiementMensuel = /*(float)*/ (loan.getAmount() * (tauxInteretMensuel / (1 - Math.pow(1 + tauxInteretMensuel, -loan.getDuration()))));
        Recoveries recoveries=new Recoveries();
        for (int mois = 1; mois <= loan.getDuration(); mois++) {
            double interets = soldeInitial * tauxInteretMensuel;
            double principal = paiementMensuel - interets;
            double soldeFinal =  (soldeInitial - principal);
            recoveries=new Recoveries(loan.getStart().plusMonths(mois), paiementMensuel,loan,interets,soldeFinal);
            System.out.println(recoveriesList);
            recoveriesList.add(recoveries);
            soldeInitial = soldeFinal;
        }
       return(List<Recoveries>) rr.saveAll(recoveriesList);


    }
    public List<Object[]> stat(){

        return rr.getRecoverySumsForCurrentYear();
    }
    public Integer latedays(int idu){
        return rr.getTotalRecoveryDaysDifferenceForCurrentUser(idu);
    }
    public List<Recoveries> getbyloan(Loan l){return rr.findByLoan(l);}
    public RecoveriesService(IRecoveriesRepository rr) {
        this.rr = rr;
    }

    @Override
    public Recoveries getRecoveriesById(int id) {
        return null;
    }

}
