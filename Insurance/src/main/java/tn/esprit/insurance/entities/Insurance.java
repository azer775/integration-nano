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

public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    int id_ass;
    String name;
    String description;
    @Enumerated(EnumType.STRING)
    TypeAss typeAss;
    float ceiling;
    String event_ass;
    String exclusions;
    String conditions;

    @OneToMany(mappedBy = "insurance", cascade ={ CascadeType.PERSIST, CascadeType.REMOVE })
    Set<Devis> devisSet;
}
