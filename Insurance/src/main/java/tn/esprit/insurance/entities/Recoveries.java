package tn.esprit.insurance.entities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.engine.spi.Status;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Recoveries implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    int id;
    LocalDate datesup;
    LocalDate daterel;
    double amount;
    double interest;
    double rest;
    @ManyToOne
    Loan loan;

    public Recoveries(LocalDate datesup, double amount, Loan loan,double interest,double rest) {
        this.datesup = datesup;
        this.amount = amount;
        this.loan = loan;
        this.interest=interest;
        this.rest=rest;
    }

    @Override
    public String toString() {
        return "Recoveries{" +
                "id=" + id +
                ", datesup=" + datesup +
                ", daterel=" + daterel +
                ", amount=" + amount +
                ", interest=" + interest +
                ", rest=" + rest +
                ", loan=" + loan +
                '}';
    }

}
