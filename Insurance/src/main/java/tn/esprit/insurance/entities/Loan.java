package tn.esprit.insurance.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.engine.spi.Status;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Loan implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private float amount;
    private int duration;
    private LocalDate Start;
    private String cin;
    private String possession;
    private String diploma;
    private String work;
    private String bankstat;
    private String state;
    @ManyToOne
    Pack pack;

    @ManyToOne
    User user;

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", amount=" + amount +
                ", duration=" + duration +
                ", Start=" + Start +
                ", pack=" + pack +
                ", user=" + user +
                '}';
    }
}
