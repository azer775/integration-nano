package tn.esprit.insurance.entities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.engine.spi.Status;

import java.io.Serializable;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Card implements Serializable {
    @Id
    int ID;
    double NumCard;
    LocalDate ExpDate;
    int SecCode;
    int CodeDab;
    @Enumerated(EnumType.STRING)
    Status status;
    @ManyToOne
    Account account;
    @ManyToOne
    OfferCard offerCard;
}
