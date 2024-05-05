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
public class OfferCard implements Serializable {
    @Id
    int ID;
    String Conditions;
    String Avantages;
    String NameOffer;
    @OneToMany(mappedBy = "offerCard")
    Set<Card> cardss;
}
