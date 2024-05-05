package tn.esprit.insurance.entities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class OfferAccount implements Serializable {
    @Id
    int ID;
    String Avantages;
    String Conditions;
    String NameOffer;
    @OneToMany(mappedBy = "offerAccount")
    Set<Account> accounts;
}
