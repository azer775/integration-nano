package tn.esprit.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.insurance.entities.Loan;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CreditScoringService {
    @Autowired
    InfoExtractor infoExtractor;
    @Autowired
    RecoveriesService recoveriesService;

    // Méthode pour calculer le score de crédit
    public int calculateCreditScore(Loan loan) {
        // Assigner des poids à chaque variable
        double weightAge = 0.2;
        double weightRatio = 0.4;
        double weightHistory = 0.4;
        int Age=calculateAge(loan.getUser().getDateOfBirth());
        double Salary= (double) infoExtractor.Salaryextractor(loan.getCin());
        double DesiredAmount= loan.getAmount();
        int TotalDaysLate=0;
        if(recoveriesService.latedays(loan.getUser().getId())!=null){
            TotalDaysLate=recoveriesService.latedays(loan.getUser().getId());
        }
        // Normaliser l'âge entre 0 et 1
        double normalizedAge = ((double) Age - 18) / (100 - 18);

        // Calculer le rapport entre le salaire et le montant voulu
        double ratio = Salary / DesiredAmount;

        // Normaliser le rapport entre 0 et 1
        double normalizedRatio = Math.min(ratio / 10, 1); // Suppose que le rapport ne dépassera pas 10

        // Calculer le score pour l'historique de paiement
        double paymentHistoryScore = calculatePaymentHistoryScore(TotalDaysLate);

        // Calculer le score total
        double scoreTotal = weightAge * normalizedAge + weightRatio * normalizedRatio + weightHistory * paymentHistoryScore;

        // Normaliser le score entre 300 et 850
        int score = (int) (scoreTotal * (850 - 300) + 300);

        return score;
    }

    // Méthode pour calculer le score basé sur l'historique de paiement
    private double calculatePaymentHistoryScore(int totalDaysLate) {
        // Supposez que pour chaque jour de retard, le score diminue de 1 point
        // Vous pouvez ajuster cette logique selon vos propres critères
        double scoreDecreasePerDay = 1.0;
        double maxScoreDecrease = 100; // Supposez qu'aucun utilisateur ne peut obtenir un retard de paiement supérieur à 100 jours

        // Calculer le score basé sur le nombre total de jours de retard
        double score = Math.max(0, 100 - Math.min(totalDaysLate * scoreDecreasePerDay, maxScoreDecrease));

        // Normaliser le score entre 0 et 1
        return score / 100;
    }
    public static int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}
