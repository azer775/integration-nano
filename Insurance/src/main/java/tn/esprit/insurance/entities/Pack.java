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
public class Pack implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String target;
    private String description;
    private float max;
    private float min;
    private String img;
    int interest;
    private boolean cin;
    private boolean possession;
    private boolean diploma;
    private boolean work;
    private boolean bankstat;
    private String name;

    @Override
    public String toString() {
        return "Pack{" +
                "id=" + id +
                ", target='" + target + '\'' +
                ", description='" + description + '\'' +
                ", max=" + max +
                ", min=" + min +
                ", interest=" + interest +
                ", cin=" + cin +
                ", possession=" + possession +
                ", diploma=" + diploma +
                ", work=" + work +
                ", bankstat=" + bankstat +
                ", name='" + name + '\'' +
                '}';
    }
    public Pack(String target, String description, float max, float min, int interest,
                boolean cin, boolean possession, boolean diploma, boolean work, boolean bankstat,
                String name) {
        this.target = target;
        this.description = description;
        this.max = max;
        this.min = min;
        this.interest = interest;
        this.cin = cin;
        this.possession = possession;
        this.diploma = diploma;
        this.work = work;
        this.bankstat = bankstat;
        this.name = name;
    }

    public Pack(String target, String description, float max, float min, String img, int interest, boolean cin, boolean possession, boolean diploma, boolean work, boolean bankstat, String name) {
        this.target = target;
        this.description = description;
        this.max = max;
        this.min = min;
        this.img = img;
        this.interest = interest;
        this.cin = cin;
        this.possession = possession;
        this.diploma = diploma;
        this.work = work;
        this.bankstat = bankstat;
        this.name = name;
    }
}
