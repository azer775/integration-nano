package tn.esprit.insurance.entities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.engine.spi.Status;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Transaction implements Serializable{
    @Id
    int ID;
    @Enumerated(EnumType.STRING)
    Methode methode;
    LocalDate dateTrans;
    @OneToMany(mappedBy = "transaction")
    Set<Titres> titreSet;
    @ManyToOne
    Account account;
}
