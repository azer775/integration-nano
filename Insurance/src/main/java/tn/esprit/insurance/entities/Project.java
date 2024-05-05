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
public class Project implements Serializable {
    @Id
    String ProjName;
    String Status;
    String Desc;
    Float CA;
    @OneToOne
    Contract contract;
}
