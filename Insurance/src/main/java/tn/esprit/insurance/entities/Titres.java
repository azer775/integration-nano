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

public class Titres implements Serializable {
    @Id
    float sicavCrois;
    float sicavRend;
    float risque;
    @ManyToOne
    Transaction transaction;
}
