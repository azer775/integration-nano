package tn.esprit.insurance.entities;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.util.Timer;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Sinister implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id_sin;
    LocalDate date_sin;
    float amount;
    @Enumerated(EnumType.STRING)
    Statut_Sin statut_sin;
    String paper;
    @Lob
    String cmnt;
    @Column(name = "type_sin")
    @Enumerated(EnumType.STRING)
    TypeSin typeSin;

    @JsonIgnore
    @ManyToOne
    Devis devis;




}
