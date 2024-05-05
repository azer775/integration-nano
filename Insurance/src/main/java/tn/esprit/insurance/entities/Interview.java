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
public class Interview implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    int id;
    LocalDate date;
    @ManyToOne
    User agent;
    @ManyToOne
    Loan loan;
   /* @ManyToMany(cascade = CascadeType.ALL)
    Set<Requirements> requirements;*/

}
