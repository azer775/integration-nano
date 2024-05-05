package tn.esprit.insurance.entities;
import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.util.Timer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Account implements Serializable {
    @Id
    String IdAccount;
    float Amount;
    float Interest_Rate;
    @Enumerated(EnumType.STRING)
    Status status;
    LocalDate CreationDate;
    @Enumerated(EnumType.STRING)
    Status Type_Account;
    double RIB;
    @OneToMany(mappedBy = "account")
    Set<Card> cards;
    @ManyToOne
    OfferAccount offerAccount;
    @OneToMany(mappedBy = "account")
    Set<Transaction> transactionSet;

}
